package com.hermes.lotcenter.infrastructure.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by liuqingqian on 2020/3/13.
 * <p>
 * Json 文本工具类
 */
@Slf4j
public class JSONTextUtil {
    public static String toJsonString(Object dto) {
        if (dto == null) {
            return null;
        }
        String json = JSON.toJSONString(dto
//                , SerializerFeature.BrowserCompatible
                , SerializerFeature.WriteMapNullValue
//                , SerializerFeature.WriteNullStringAsEmpty
                , SerializerFeature.DisableCircularReferenceDetect);
        return json;
    }

    public static <T> T parseObject(String text, Class<T> clazz) {

        if (StringUtils.isEmpty(text)) {
            return null;
        }
        T dto = null;
        try {
            dto = JSON.parseObject(text, clazz);
        } catch (Exception e) {
            log.error("【JSON 解析异常】e:", e);
        }
        return dto;
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {

        if (StringUtils.isEmpty(text)) {
            return null;
        }
        List<T> list = null;
        try {
            list = JSON.parseArray(text, clazz);
        } catch (Exception e) {
            log.error("【JSON 解析异常】e:", e);
        }
        return list;
    }

    public static JSONArray parseArray(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        JSONArray array = null;
        try {
            array = JSON.parseArray(text);
        } catch (Exception e) {
            log.error("【JSON 解析异常】e:", e);
        }
        return array;
    }

    public static JSONArray convertLinkedHashMapArray(JSONArray array) {
        if (CollectionUtils.isEmpty(array)) {
            return new JSONArray();
        }
        return parseArray(toJsonString(array));
    }

}
