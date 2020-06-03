package com.hc.help;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/20.
 */
public class Json implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6330204587337683405L;
	private String message;
    private String code = "200";
    private Object data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Json(){}

    public static Json success() {
        return success(null);
    }

    public static Json success(Object data) {
        Json json = new Json();
        json.setCode("200");
        json.setMessage("success");
        json.setData(data);
        return json;
    }

    public static Json success(String message) {
        Json json = new Json();
        json.setCode("200");
        json.setMessage(message);
        return json;
    }

    public static Json success(Object data, String message) {
        Json json = new Json();
        json.setCode("200");
        json.setMessage(message);
        json.setData(data);
        return json;
    }

    public static Json error() {
        return error(null);
    }

    public static Json error(String message) {
        return error("500", message);
    }

    public static Json error(String code, String message) {
        Json json = new Json();
        json.setCode(code);
        json.setMessage(message);
        json.setData(null);
        return json;
    }
}
