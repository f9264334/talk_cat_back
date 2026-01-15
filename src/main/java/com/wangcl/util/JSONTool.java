/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.wangcl.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.security.MessageDigest;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuzeyu on 2017/6/19.
 */
public class JSONTool {
    private static final Logger logger = LoggerFactory.getLogger(JSONTool.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectMapper extendMapper = new ObjectMapper();


    static {
        //序列化时，遇到空bean（无属性）时不会失败
        extendMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //反序列化时，遇到未知属性（在bean上找不到对应属性）时不会失败
        extendMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //反序列化时，将空数组([])当做null来处理（以便把空数组反序列化到对象属性上——对php生成的json的map属性很有用）
        extendMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        //不通过fields来探测（仅通过标准getter探测）
        extendMapper.configure(MapperFeature.AUTO_DETECT_FIELDS, false);

        extendMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        extendMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
    }

    static {
        //序列化时，跳过null属性
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //序列化时，遇到空bean（无属性）时不会失败
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //反序列化时，遇到未知属性（在bean上找不到对应属性）时不会失败
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //反序列化时，将空数组([])当做null来处理（以便把空数组反序列化到对象属性上——对php生成的json的map属性很有用）
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        //不通过fields来探测（仅通过标准getter探测）
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, false);
    }

    /* ====================== 反序列化工具 ==================== */

    /**
     * Json串转为对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parse(String json, TypeReference<T> type) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.warn("parse failed. json={} type={} msg={}", json, type.getType(), e.getMessage());
            return null;
        }
    }


    public static String md5ToLong(String s) {

        if (StringUtils.isEmpty(s)) {
            return null;
        }
        // 这里根据MD5时间的值更换16进制abc的大小写
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            char str[] = new char[md.length];
            int k = 0;
            for (int i = 0; i < md.length; i++) {
                byte byte0 = md[i];
                //只取高位
                str[k++] = hexDigits[(byte0 >>> 4 & 0xf) % 10];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String estateNameRegex(String name) {
        Pattern p = Pattern.compile("^(.*?)(\\d+号)*(\\d+期)*(\\(.*\\))*$");
        Matcher matcher = p.matcher(name);
        if (!matcher.matches()) {
            System.out.println(matcher.matches());
            return null;
        }
        return matcher.group(1);
    }

    public static void main(String[] args) {
        System.out.println(estateNameRegex("发发地方12号"));
        System.out.println(estateNameRegex("发发地方12期"));
        System.out.println(estateNameRegex("发发地方12号(大声道)"));
        System.out.println(estateNameRegex("发发地方(大声道)"));
        System.out.println(estateNameRegex("发发地方"));

        System.out.println(Long.valueOf(md5ToLong(("发发地方"))));
        System.out.println(Long.valueOf(md5ToLong("206390230031_hx_http://hwxypic.5i5j.com/images/chengdudir/20230625/d909924aa7334579af307ab07fa884ab.jpg")));
        System.out.println(Long.valueOf(md5ToLong("206390230031_ws_http://hwxypic.5i5j.com/images/chengdudir/20230625/d909924aa7334579af307ab07fa884ab.jpg")));
        System.out.println(Long.valueOf(md5ToLong("206379565777_ws_http://hwxypic.5i5j.com/images/chengdudir/20230625/d909924aa73d579af307ab07fa884ab.jpg")));
    }


    public static <T> T parse2(String json, Class<T> type) throws IOException {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return mapper.readValue(json, type);
    }

    /**
     * Json串转为对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parse(String json, Class<T> type) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.warn("parse failed. json={} type={} msg={}", json, type, e.getMessage());
            return null;
        }
    }


    /**
     * 输入流转为对象
     *
     * @param stream
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parse(InputStream stream, TypeReference<T> type) {
        try {
            return mapper.readValue(stream, type);
        } catch (IOException e) {
            logger.warn("parse failed. type={} msg={}", type.getType(), e.getMessage());
            return null;
        }
    }

    /**
     * 输入流转为对象
     *
     * @param stream
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parse(InputStream stream, Class<T> type) {
        try {
            return mapper.readValue(stream, type);
        } catch (IOException e) {
            logger.warn("parse failed. type={} msg={}", type, e.getMessage());
            return null;
        }
    }

    /* ====================== 序列化工具 ==================== */

    /**
     * 序列化对象转为json-string
     *
     * @param target
     * @return
     */
    public static String writeToString(Object target) {
        if (target == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(target);
        } catch (JsonProcessingException e) {
            logger.warn("writeToString failed. target={} msg={}", target.getClass(), e.getMessage());
            return "";
        }
    }

    public static String writeToStringAll(Object target) {
        try {
            return extendMapper.writeValueAsString(target);
        } catch (JsonProcessingException e) {
            logger.warn("writeToStringAll failed. target={} msg={}", target.getClass(), e.getMessage());
            return "";
        }
    }


    /**
     * 序列化对象并写入Writer
     *
     * @param writer
     * @param target
     * @throws IOException
     */
    public static void write(Writer writer, Object target) throws IOException {
        mapper.writeValue(writer, target);
    }

    /**
     * 序列化对象并写入Stream
     *
     * @param stream
     * @param target
     * @throws IOException
     */
    public static void write(OutputStream stream, Object target) throws IOException {
        mapper.writeValue(stream, target);
    }

    public static Map<String, Object> objectToMap(Object object) {
        return mapper.convertValue(object, new TypeReference<Map<String, Object>>() {
        });
    }
}
