#!/bin/env bash
#############################################
## main
## 非托管方式, 启动服务
## control.sh脚本, 必须实现start和stop两个方法
#############################################


WORKSPACE=`cd $(dirname $0);pwd`
cd ${WORKSPACE}

# service info
app=${}
JAVA_OPTS=`cat jvm.options`
SVCOUTLOG="${app}.out"
SVCERRLOG="${app}.err"

NGINXFUNC=./nginxfunc.sh
source ${NGINXFUNC}
NGINX_CHECK=1
LOCAL_CHECK=1


# params
ACTION=${1}

# start
start() {
    # check nohup
    NOHUP=`which nohup`
    if [[ $? -ne 0 ]];then
        echo "nohup not found."
        exit -2
    fi
    ${NOHUP} java ${JAVA_OPTS} $JAVA_TOOL_OPTIONS -jar ${WORKSPACE}/${app}.jar > /dev/null 2>${WORKSPACE}/${SVCERRLOG} &
    sleep 3
    status
}

# stop
timeout=60
function stop(){
    # 循环stop服务, 直至60s超时
    for (( i = 0; i < $timeout; i++ )); do
        # 检查服务是否停止,如果停止则直接返回
        check_pid
        if [ $? -eq 0 ];then
           echo "${app} is stopped"
           return 0
        fi
        # 检查pid是否存在
        local pid=$(get_pid)
        if [ "x_${pid}" == "x_" ];then
           echo "${app} is stopped, can't find pid on ${pidfile}"
           exit 0
        fi

        # 停止该服务
        if [ $i -eq $((timeout-1)) ]; then
            kill -9 ${pid} &>/dev/null
        else
            kill ${pid} &>/dev/null
        fi
        # 检查该服务是否停止ok
        check_pid
        if [ $? -eq 0 ];then
            # stop服务成功, 返回码为 0
            echo "${app} stop ok"
            return 0
        fi
        # 服务未停止, 继续循环
        sleep 1
    done
    # stop服务失败, 返回码为 非0
    echo "stop timeout(60s)"
    exit 1

}


# get pid
get_pid() {
    pid=`ps aux | grep "/${app}.jar" | grep -v grep | awk '{print $2}'`
    if [ -z ${pid} ]; then
        echo ""
    fi
    echo ${pid}
}

function check_pid(){
    pid=$(get_pid)
    if [ "x_" != "x_${pid}" ]; then
        running=$(ps -p ${pid}|grep -v "PID TTY" |wc -l)
        return ${running}
    fi
    return 0
}


# status
status() {
    local pid=$(get_pid)
    if [[ -z $pid ]];then
        echo "${app}.jar not running."
    else
        echo "${app}.jar running, pid: ${pid}"
    fi
}


# display
display() {
    echo "usage: sh control.sh [start|stop|restart|status]"
}

case ${ACTION} in
    "start")
    start
    sleep 10
	http_start
        ;;
    "stop")
	    http_stop
        stop
        ;;
    "restart")
        http_stop
        stop
        sleep 5
        start
        http_start
        ;;
    "frestart")
        restart
        ;;
    "status")
        status
        ;;
    *)
        display
        ;;
esac
