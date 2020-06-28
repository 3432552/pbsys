package com.hzy.manager.common;

import java.util.HashMap;

/**
 * 统一数据返回类
 */
public class Result extends HashMap<String, Object> {
    public Result() {
        put("code", 200);
        put("msg", "操作成功");
    }

    public static Result ok() {
        return new Result();
    }

    public static Result ok(String msg) {
        Result result = new Result();
        result.put("code", 200);
        result.put("msg", msg);
        return result;
    }

    public static Result ok(Object data) {
        Result result = new Result();
        result.put("code", 200);
        result.put("data", data);
        return result;
    }

    public static Result error(Object msg) {
        Result result = new Result();
        result.put("code", 500);
        result.put("msg", msg);
        return result;
    }

    public static Result wan(Object msg) {
        Result result = new Result();
        result.put("code", 400);
        result.put("msg", msg);
        return result;
    }
}
