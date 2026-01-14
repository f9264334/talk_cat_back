package com.wangcl.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

/**
 * 功能描述：
 *
 * @author wangchenglong
 * @version 1.0
 * @date 2026/1/14 17:14
 */
public class JSONTool {
    // 全局共享 ObjectMapper（线程安全，无需频繁创建）
    private static final ObjectMapper OBJECT_MAPPER;

    // 静态初始化：配置 ObjectMapper 全局参数
    static {
        OBJECT_MAPPER = new ObjectMapper();

        // 1. 序列化配置：美化输出、支持 JDK8 时间类型（LocalDateTime/LocalDate 等）
        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT) // 美化 JSON 格式（可选）
                .registerModule(new JavaTimeModule()) // 支持 Java 8 时间API
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS) // 空对象不报错
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 时间序列化为字符串（而非时间戳）

        // 2. 反序列化配置：忽略未知字段、空字符串转 null 等
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // 忽略 JSON 中不存在的类字段
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) // 空字符串转 null
                .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES); // 基本类型字段为 null 不报错
    }

    // 私有构造器：禁止实例化工具类
    private JSONTool() {
        throw new UnsupportedOperationException("JSONTool 是工具类，禁止实例化！");
    }

    // ====================== 核心方法1：对象 → JSON 字符串 ======================

    /**
     * 将单个对象转为 JSON 字符串
     * @param obj 待转换的对象（支持任意 POJO、集合、Map 等）
     * @return JSON 字符串
     * @throws RuntimeException 转换失败时抛出运行时异常（简化调用方处理）
     */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转 JSON 字符串失败！目标对象：" + obj, e);
        }
    }

    // ====================== 核心方法2：JSON 字符串 → 指定类对象 ======================

    /**
     * JSON 字符串转为单个指定类型的对象
     * @param json JSON 字符串
     * @param clazz 目标类的 Class 对象
     * @param <T> 目标类型泛型
     * @return 目标类对象
     * @throws RuntimeException 转换失败时抛出运行时异常
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON 字符串不能为空！");
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 字符串转对象失败！JSON：" + json + "，目标类：" + clazz.getName(), e);
        }
    }

    /**
     * JSON 字符串转为 List 集合（泛型友好）
     * @param json JSON 字符串
     * @param elementClazz List 中元素的类对象
     * @param <T> 元素类型泛型
     * @return List<T> 集合
     * @throws RuntimeException 转换失败时抛出运行时异常
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> elementClazz) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON 字符串不能为空！");
        }
        try {
            // 构建 List 类型的 TypeReference，解决泛型擦除问题
            return OBJECT_MAPPER.readValue(json,
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, elementClazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 字符串转 List 失败！JSON：" + json + "，元素类型：" + elementClazz.getName(), e);
        }
    }
}
