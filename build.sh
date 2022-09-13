#!/bin/env bash
# Setup basic environment
WORKSPACE=$(cd "$(dirname "$0")"; pwd)

# change dir
cd ${WORKSPACE}

# base info
MODULE=
APP="lottery-center"
CONTROL=./control.sh
OUTPUT="./output"
DEPLOYDIR="/Users/liuqingqian/work/hermes/app"
gitversion=RELEASE


env=$1


case $env in
    "dev")
        ENVIRONMENT="dev";;
    "prod")
        ENVIRONMENT="prod";;
    "pre")
        ENVIRONMENT="pre";;
    "test")
        ENVIRONMENT="test";;
    *)
        ENVIRONMENT="test";;
esac

function build() {
    # has mvn
    mvn -version > /dev/null 2>&1
    if [[ $? -ne 0 ]];then
        export MVN_HOME=/home/beicai/apache-maven-3.6.0/
        export PATH=$MVN_HOME/bin:$PATH
    fi
    # maven package
    mvn -Dorg.slf4j.simpleLogger.defaultLogLevel=warn clean package -P "${ENVIRONMENT}" -T 24 -Dmaven.test.skip=true
    local sc=$?
    if [ $sc -ne 0 ];then
        ## 编译失败, 退出码为 非0
        echo "$APP build error"
        exit $sc
    else
        echo -n "$APP build ok, vsn="
	    rm -rf $gitversion
        get_gitversion
    fi

}

function make_output() {
    # clean
    if [[ ! -e ${OUTPUT} ]];then
	    mkdir -p ${OUTPUT}
    fi
    rm -rf ${OUTPUT}/* > /dev/null 2>&1
    #
    (
        mv ./target/*.jar ${OUTPUT}/ && \
        cp ${CONTROL} ${OUTPUT}/ && \
        cp nginxfunc.sh ${OUTPUT}/ && \
        #兼容ECS发
        if [ "$ENVIRONMENT" == "prod" ];then
            cp conf/${ENVIRONMENT}/nginx/nginx.conf ${OUTPUT}/
            cp conf/${ENVIRONMENT}/server/jvm.options ${OUTPUT}/
        else
            cp jvm.options.${ENVIRONMENT} ${OUTPUT}/jvm.options
            mv ${OUTPUT}/*.jar ${OUTPUT}/${APP}.jar
        fi
	cp $gitversion ${OUTPUT} && \
        echo -e "make output ok."
    ) || ( echo -e "make output failed!"; exit 2; )
}


function get_gitversion() {
    version=$(git status | sed -n '1p' | awk '{print $NF}')
    echo "Version: ${version}" > $gitversion
    revision=$(git rev-parse HEAD)
    echo "Revision: ${revision}" >> $gitversion
    local gv=`cat $gitversion`
    echo "$gv"
}

function copy_deploy() {
  rm -rf ${DEPLOYDIR}/${APP}
  mv ${OUTPUT}/ ${DEPLOYDIR}/${APP}/
}


#
build

#
make_output

copy_deploy
#
echo -e "build done."
exit 0



