package com.demon.app.domain.response;

import com.demon.app.domain.SerializeCloneable;

/**
 * Created: KimChheng
 * Date: 27-Oct-2021 Wed
 * Time: 9:13 PM
 */
public class ResponseWrapper implements SerializeCloneable {

    private static final long serialVersionUID = -1641100456165479801L;

    private String code;
    private String message;
    private Object data;

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
