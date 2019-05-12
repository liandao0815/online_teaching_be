package com.liandao.onlineteaching.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    public static Map<String, Object> success(Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("data", data);

        return map;
    }

    public static Map<String, Object> fail(Object message) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("message", message);

        return map;
    }

    public static Map<String, Object> fail(int code, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);

        return map;
    }
}
