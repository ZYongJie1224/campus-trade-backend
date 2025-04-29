package com.nuomi.backend.model;

import lombok.Data;

/**
 * 通用REST API响应体，建议直接作为Controller返回类型，前端可无脑解构。
 * code: 状态码（如200, 400, 500等）
 * message: 文字描述
 * data: 任意类型业务数据（可为null）
 */
@Data
public class QuickResponse<T> {
    private int code;
    private String message;
    private T data;

    public QuickResponse() {}

    public QuickResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public QuickResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    // 工厂方法：成功
    public static <T> QuickResponse<T> success(T data, String message) {
        return new QuickResponse<>(200, data, message);
    }
    public static <T> QuickResponse<T> success(String message) {
        return new QuickResponse<>(200, null, message);
    }

    // 工厂方法：失败
    public static <T> QuickResponse<T> fail(int code, String message) {
        return new QuickResponse<>(code, null, message);
    }
}