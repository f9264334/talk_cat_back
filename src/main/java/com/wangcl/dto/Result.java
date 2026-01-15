package com.wangcl.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 功能描述：
 *
 * @author wangchenglong
 * @version 1.0
 * @date 2026/1/14 16:23
 */
@Data
@Builder
public class Result {
    private String message;
    private Integer code;
    private Object data;
    public static Result success(Object data ){
        return  Result.builder().code(200).message("").data(data).build();
    }
    public static Result error(Integer code, String message){
        return  Result.builder().code(code).message(message).data(null).build();
    }
}
