package com.nebulaxy.milimilibackendmain.exception;

/**
 * 自定义异常
 * 运行时异常
 */
public class CustomerException extends RuntimeException {
    private String message;
    private String code;

    public CustomerException(String code, String message) {
        this.message = message;
        this.code = code;
    }

    // 新增构造函数：支持传递原始异常（关键修复）
    public CustomerException(String message, Throwable cause) {
        super(message, cause); // 父类保存 message 和 cause
        this.code = "500";     // 默认错误码
    }

    public CustomerException(String message) {
        this.message = message;
        this.code = "500";
    }

    public CustomerException() {}

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
