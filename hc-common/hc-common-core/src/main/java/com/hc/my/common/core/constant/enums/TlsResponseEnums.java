package com.hc.my.common.core.constant.enums;
/**
 * @author LiuZhiHao
 * @date 2019/10/21 14:10
 * 描述:
 **/
public enum TlsResponseEnums {
    SYSTEM_ERROR(001,"系统异常"),
    BAD_REQUEST(002,"错误的请求参数"),
    NOT_FOUND(003,"找不到请求路径！"),
    CONNECTION_ERROR(004,"网络连接请求失败！"),
    METHOD_NOT_ALLOWED(444,"token信息不存在"),
    TOKEN_ERROR(006,"Token异常"),
    TOKEN_LOST_ERROR(666,"token过期了！"),
    REPEAT_REGISTER(8,"用户名重复"),
    NO_USER_EXIST(9,"用户不存在"),
    INVALID_PASSWORD(00010,"密码错误"),
    NO_PERMISSION(00011,"非法请求！"),
    SUCCESS_OPTION(00012,"操作成功！"),
    NOT_MATCH(00013,"用户名和密码不匹配"),
    FAIL_GETDATA(00014,"删除信息失败,患者已关联培养皿"),
    BAD_REQUEST_TYPE(00015,"错误的请求类型"),
    INVALID_MOBILE(00016,"无效的手机号码"),
    INVALID_EMAIL(00017,"无效的邮箱"),
    INVALID_GENDER(18,"无效的性别"),
    REPEAT_MOBILE(19,"已存在此手机号"),
    REPEAT_EMAIL(00020,"已存在此邮箱地址"),
    NO_RECORD(00021,"没有查到相关记录"),
    LOGIN_SUCCESS(00022,"登陆成功"),
    LOGOUT_SUCCESS(00023,"已退出登录"),
    SENDEMAIL_SUCCESS(00024,"邮件已发送，请注意查收"),
    EDITPWD_SUCCESS(00025,"修改密码成功"),
    No_FileSELECT(00026,"未选择文件"),
    FILEUPLOAD_SUCCESS(00027,"上传成功"),
    NOLOGIN(28,"未登陆"),
    ILLEGAL_ARGUMENT(29,"病历号重复"),
    ERROR_IDCODE(00030,"验证码不正确");

    private int code;
    private String msg;
    TlsResponseEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }


}
