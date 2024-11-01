package com.slz.xblog.utils;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/23
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 配置 ObjectMapper
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 忽略未知属性
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true); // 使用枚举的 toString()
    }

    // 对象转 JSON 字符串
    public static String objectToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null; // 或者可以抛出一个自定义异常
        }
    }

    // JSON 字符串转对象
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null; // 或者可以抛出一个自定义异常
        }
    }
}

