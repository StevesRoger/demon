package com.demon.auth.domain.response;

import com.demon.auth.domain.SerializeCloneable;

public class ResponseBody implements SerializeCloneable {

    private static final long serialVersionUID = -1641100456165479801L;

    private String code = "S200";
    private String message;
    private Object data;

    public ResponseBody(String message) {
        this.message = message;
    }

    public ResponseBody(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public ResponseBody(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
