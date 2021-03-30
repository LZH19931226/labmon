package com.hc.my.common.core.bean;

import com.hc.my.common.core.constant.enums.TlsResponseEnums;

import java.io.Serializable;

/**
 * @author LiuZhiHao
 * @date 2019/8/26 18:03
 * 描述:接口返回对象封装bean结构
 **/
@SuppressWarnings({"unchecked", "rawtypes"})
public class ApiResponse<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1272926047811431195L;
    //成功
    public final static int SUCCESS = 200;
    //失败
    public final static int FAILED = 500;
    //参数错误
    public final static int PARAM_ERROR = 400;
    //授权认证失败
    public final static int AUTH_FAILED = 999;
    // 无权限访问
    public final static int FORBIDDEN = 403;
    // 未找到资源
    public final static int NOT_FOUND = 404;

    /**
     * 返回消息code编码
     * 200：表示接口请求成功
     * 201：表示接口请求成功，但是有新的版本更新，可做更新操作，会返回请求数据
     * 202：表示接口请求成功，但是有新的版本更新，必须做版本更新操作，不会返回请求数据
     * 999：表示用户身份验证失败，需要重新登陆授权
     * 500和其它值：表示接口请求失败
     */
    private int code = SUCCESS;
    //返回消息内容
    private String message = "请求成功！";
    //返回结果对象
    private T result;

    private boolean success;


    public ApiResponse() {
    }

    public ApiResponse(boolean success, T data, int code, String errMsg) {
        super();
        this.success = success;
        this.result = data;
        this.code = code;
        this.message = errMsg;
    }

    public ApiResponse(boolean success, int code, String mag){
        this.success=success;
        this.code=code;
        this.message=mag;
    }

    public ApiResponse(boolean success, T data, TlsResponseEnums enums){
        this.success=success;
        this.result=data;
        this.code=enums.getCode();
        this.message=enums.getMsg();
    }
    public ApiResponse(boolean success, TlsResponseEnums enums){
        this.success=success;
        this.code=enums.getCode();
        this.message=enums.getMsg();
    }
    public ApiResponse(boolean success, String message){
        this.success=success;
        this.message=message;
        this.code=500;
    }

    public ApiResponse(T result) {
        this.result = result;
    }


    public static ApiResponse<String> success(String msg) {
        ApiResponse<String> res = new ApiResponse<>();
        res.setMessage(msg);
        return res;
    }

    public static <T> ApiResponse<T> successs(T result) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setMessage("操作成功");
        res.setResult(result);
        return res;
    }

    public static ApiResponse<String> saveSuccess() {
        return success("数据保存成功！");
    }

    public static ApiResponse<String> deleteSuccess() {
        return success("数据删除成功！");
    }

    public static ApiResponse<String> updateSuccess() {
        return success("数据修改成功！");
    }

    public static ApiResponse<String> success() {
        return success("操作成功");
    }

    public static ApiResponse<String> fail(String msg) {
        ApiResponse<String> res = new ApiResponse<>();
        res.setCode(FAILED);
        res.setMessage(msg);
        return res;
    }

    public static ApiResponse<String> notFound(String msg) {
        ApiResponse<String> res = new ApiResponse<>();
        res.setCode(NOT_FOUND);
        res.setMessage(msg);
        return res;
    }

    public static ApiResponse<String> paramError(String msg) {
        ApiResponse<String> res = new ApiResponse<>();
        res.setCode(PARAM_ERROR);
        res.setMessage(msg);
        return res;
    }

    public static ApiResponse<String> fail() {
        return fail("操作失败");
    }

    public static ApiResponse<String> error() {
        return fail("未知错误");
    }

    public static ApiResponse<String> forbidden() {
        ApiResponse<String> res = new ApiResponse<>();
        res.setCode(FORBIDDEN);
        res.setMessage("无权限访问");
        return res;
    }

    public boolean isSuccess() {
        return this.code == 200;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }


    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}