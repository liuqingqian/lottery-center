package com.hermes.lotcenter;

import com.hermes.lotcenter.infrastructure.utils.NetUtil2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuqingqian on 2022/9/7.
 */
public class TestLotteryNetUtil {

    //Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
//Accept-Encoding: gzip, deflate
//Accept-Language: zh-CN,zh;q=0.9,en;q=0.8
//Cache-Control: no-cache
//Cookie: SESSION=bf0d036e-5500-497f-a626-3ef89fc6cd53
//Host: 8585055.cc
//Pragma: no-cache
//Proxy-Connection: keep-alive
//Upgrade-Insecure-Requests: 1
//User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36
    public static void main(String[] args) throws IOException {
        post() ;
    }


    public static void get() {
        //TODO:1、批量获取html文件存储；3、转换成DTO->Entity入库

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headers.put("Pragma", "no-cache");
        headers.put("Cache-Control", "no-cache");
        headers.put("Proxy-Connection", "keep-alive");
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");

        String url = "http://8585055.cc/lotteryV2/resultList.do";
        Map<String, Object> params = new HashMap<>();
        String lotCode = "AZK3";
        String startDate = "2022-08-23";
        params.put("lotCode", lotCode);
        params.put("startDate", startDate);
        NetUtil2.Response res = NetUtil2.get(url, params, headers);
        System.out.println(res.rawContent);
    }

    //    Accept: application/json, text/javascript, */*; q=0.01
//Accept-Encoding: gzip, deflate
//Accept-Language: zh-CN,zh;q=0.9,en;q=0.8
//Cache-Control: no-cache
//Host: ft5812.com
//Origin: http://ft5812.com
//Pragma: no-cache
//Proxy-Connection: keep-alive
//Referer: http://ft5812.com/lotteryV2/index.do
//User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36
//X-Requested-With: XMLHttpRequest
    public static void post() {


        String origin = "http://ft5812.com";
        String referer = origin + "/lotteryV2/index.do";
        String url = origin + "/lotteryV2/lotV2Op.do?lotCode=FFK3";

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headers.put("Origin", origin);
        headers.put("Pragma", "no-cache");
        headers.put("Cache-Control", "no-cache");
        headers.put("Proxy-Connection", "keep-alive");
        headers.put("Referer", referer);
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, Object> params = new HashMap<>();
        String lotCode = "FFK3";
        params.put("lotCode", lotCode);
        NetUtil2.Response res = NetUtil2.post(url, params, headers);
        System.out.println(res.rawContent);
    }
}
