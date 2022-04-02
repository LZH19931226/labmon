package com.hc.interpector;

/**
 * 项目名称：全局变量
 *
 * @author:lyo
 */
public class Const {

    public static final String PATIENT_TOKEN = "patientToken"; //患者用户
    public static final String DOCTOR_TOKEN = "doctorToken"; //医生用户

    public static final String SYS_USER = "sysUser"; //平台用户
    public static final String SYS_ROLE = "sysRole"; //平台用户角色
    public static final String SYS_MENUS = "sysMenus"; //平台用户菜单
    public static final String PATIENT_TYPE = "patient";

    public static final String DOCTOR_TYPE = "doctor";
    public static final String CHARSET = "UTF-8";// 当前系统编码

    //附件类型
    public static final String ATTACHMENT_TYPE_HEADER = "header";//附件头像
    public static final String ATTACHMENT_TYPE_ROLL_PIC = "roll_pic";//宣传栏滚动图


    public static final String USER_TYPE_HOSPITAL = "hospital";//生殖中心账号
    public static final String USER_TYPE_SYSTEM = "system";//系统账号


    public static final String GOOD_NEW_TYPE_CHILDBIRTH = "childbirth";//分娩
    public static final String GOOD_NEW_TYPE_PREGNANT = "pregnant";//怀孕
    /**
     * 存储当前登录用户id的字段名
     */
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";

    /**
     * token有效期（小时）
     */
    public static final int TOKEN_EXPIRES_HOUR = 12;

    /**
     * 存放Authorization的header字段
     */
    public static final String AUTHORIZATION = "authorization";

    /**
     * 存放Authorization的header字段
     */
    public static final String TOKEN = "token";


    /**
     * 不对匹配该值的访问路径拦截（正则）
     */
    public static final String NO_INTERCEPTOR_PATH = ".*/((token)|(login)|(userinfo/userLogin)|(loginTo)|(register)|(validateCode)|(getInfo)|(findPwd)|(validate)|(bind)|(oauth*)|(/statistical/*)|([a-zA-Z0-9]{1,20}\\.html)).*";

    public static final String API_PATH = ".*/((api)).*";


    public static final String WEIXIN = "weixin"; //微信

    public static final String WEIXIN_WEB = "wxWeb"; //微信

    public static final String QQ = "qq";  //qq

    public static final String FILEPATHIMG = "uploadFiles/uploadImgs/";    //图片上传路径
    public static final String FILEPATHFILE = "uploadFiles/files/";        //文件上传路径

    public static final int THREADCOUNT = 10;

    /**
     * jwt
     */
    public static final String JWT_ID = "jwt";
    public static final String JWT_SECRET = "7786df7fc3a34e26a61c034d5ec8245d";
    public static final int JWT_TTL = 60 * 60 * 1000;  //millisecond
    public static final int JWT_REFRESH_INTERVAL = 55 * 60 * 1000;  //millisecond
    public static final int JWT_REFRESH_TTL = 12 * 60 * 60 * 1000;  //millisecond


    public static final int RESULT_STATUS_SUCCESS = 200;// 成功
    public static final int RESULT_STATUS_AUTHORITY = 502;// 权限管理消息提示（代码：502）例：用户被禁言，误操作（想删除别人的帖子）
    public static final int RESULT_STATUS_PRECONDITION_FAILED = 412;//操作参数未满足消息提示（代码：412）	例：提交的某个参数为空
    public static final int RESULT_STATUS_TOKEN_INVALID = 401;//关于token 消息提示（代码：401） 例：包括token失效，token为空
    public static final int RESULT_STATUS_SYS_ERROR = 501;  //系统错误消息提示（代码：501）例：代码执行错误
    public static final int RESULT_STATUS_NOT_FOUND = 404;
    public static final int RESULT_STATUS_OTHER_ERROR = 505;//其他（代码：505）
    public static final int RESULT_STATUS_NOT_DANDLE = 808; //异常不处理
    public static final int RESULT_STATUS_HX_CREATE_FAILED = 600;//环信账号创建失败
    public static final int RESULT_STATUS_HX_IN_GROUP_FAILED = 601;//用户加群失败
    public static final int RESULT_STATUS_HX_OUT_GROUP_FAILED = 602;//用户加群失败

    public static final int RESULT_STATUS_FIND_IS_NULL = 603;//查询结果为空；
    public static final int RESULT_STATUS_FIND_IS_0 = 604;//查询结果为0；
    public static final int RESULT_STATUS_NO_DATA_FIND = 605;//查询结果为空或为0

    /**
     * "身份信息"参数名称
     */
    public static final String PRINCIPAL_ATTRIBUTE_NAME = "Member.PRINCIPAL";

    /**
     * "用户名"Cookie名称
     */
    public static final String COOKIE_USERNAME_NAME = "hu_username";

    /**
     * "用户名"Cookie名称
     */
    public static final String COOKIE_PASS_NAME = "hu_sp";

    /**
     * "用户名"Cookie名称
     */
    public static final String HIS_URL = "http://120.24.79.125:8093";

    /**
     * 短信发送请求
     */
    public static final String NOTICE_SMS_URL = "https://www.ivfcn.com/interface/hospital/smsUtil.do";

    /**
     * 亿美短信状态接口
     */
    public static final String STATUS_SMS_URL = "http://192.168.0.107:8094/SmsSend/getStatusReport";

    /**
     * 推送消息接口
     */
    public static final String NOTIFICATION_PUSH_URL = "https://www.ivfcn.com/interface/hospital/umengPush.do";
//    public static final String NOTIFICATION_PUSH_URL = "https://www.ivfcn.com/interface/hospital/umengPush.do";

    //二维码保存物理路径
    public static final String QRCODE_ENCODE_PATH = "http://www.ivfcn.com/saoma/view/serviceReview.html";

    //二维码保存物理路径
    public static final String FILE_UPLOAD_PATH = "http://192.168.0.104:8086/api/fileUpload/singleImg";

    //老服务pingpay退款地址
    public static final String PINGPAY_WITHDRAW_URL = "http://www.ivfcn.com/consultManager/payBack.do";


    public static final String SMS_SEND_URL = "http://120.24.79.125:8094/alysms";

    public static final String DYNAMIC_PRODUCER_QUEUE_URL = "http://120.24.79.125:8089/task_service/api/mq/producer";
    public static final String DYNAMIC_CONSUMER_QUEUE_URL = "http://120.24.79.125:8089/task_service/api/mq/consumer";
//    public static final String DYNAMIC_QUEUE_URL = "http://192.168.0.118:8089/task_service/api/mq/producer";
}
