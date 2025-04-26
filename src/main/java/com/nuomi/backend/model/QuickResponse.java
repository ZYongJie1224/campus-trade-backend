package com.nuomi.backend.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.context.annotation.Bean;

@Data

public class QuickResponse {
    @Bean
    public JSONObject  getQuickResponse(int code, String message) {
        JSONObject obj = new JSONObject();
        obj.put("code", code);
        obj.put("message", message);
        return obj;
    }
    @Bean
    public JSONObject  getQuickDataResponse(int code, Object data, String message) {
        JSONObject obj = new JSONObject();
        obj.put("code", code);
        obj.put("data", data);
        obj.put("message", message);
        return obj;
    }
}
