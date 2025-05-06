package com.nebulaxy.milimilibackendmain.common;

public class Result {
    private String code;
    private Object data;
    private String message;

    public static Result success() {
        Result result = new Result();
        result.setCode("200");
        result.setMessage("success");
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode("200");
        result.setData(data);
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.setCode("500");
        result.setMessage(message);
        return result;
    }

    public static Result error(String code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
