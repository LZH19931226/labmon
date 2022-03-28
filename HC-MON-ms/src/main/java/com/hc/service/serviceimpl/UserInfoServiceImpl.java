package com.hc.service.serviceimpl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.github.pagehelper.Page;
import com.hc.web.config.RedisTemplateUtil;
import com.hc.dao.HospitaDao;
import com.hc.dao.UserrightDao;
import com.hc.entity.Hospitalofreginfo;
import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorupsrecord;
import com.hc.entity.Userright;
import com.hc.mapper.laboratoryFrom.ClientInfoMapper;
import com.hc.mapper.laboratoryFrom.EquipmentTypeMapper;
import com.hc.mapper.laboratoryFrom.UserInfoFromMapper;
import com.hc.model.LoginResponseModel;
import com.hc.model.MonitorequipmenttypeModel;
import com.hc.model.UserModel;
import com.hc.service.EquipmentInfoService;
import com.hc.service.UserInfoService;
import com.hc.utils.ApiResponse;
import com.hc.utils.HttpUtil;
import com.hc.utils.JsonUtil;
import com.hc.utils.TokenHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by 16956 on 2018-07-31.
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    private UserInfoFromMapper userInfoFromMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private UserrightDao userrightDao;
    @Autowired
    private EquipmentTypeMapper equipmentTypeMapper;
    @Autowired
    private HospitaDao hospitaDao;
    @Autowired
    private ClientInfoMapper clientInfoMapper;
    @Autowired
    private EquipmentInfoService equipmentInfoService;


    @Override
    public ApiResponse<LoginResponseModel> userLoginByApp(UserModel userModel) {
        ApiResponse<LoginResponseModel> apiResponse = new ApiResponse<LoginResponseModel>();
        LoginResponseModel loginResponseModel = new LoginResponseModel();
        Userright userright = new Userright();
        try{
            userright = userInfoFromMapper.userlogin(userModel);
            if (StringUtils.isEmpty(userright)) {
                apiResponse.setMessage("用户名或密码错误");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            String userid = userright.getUserid();
            List<MonitorequipmenttypeModel> monitorequipmenttypeModels = new ArrayList<MonitorequipmenttypeModel>();
            if (!StringUtils.isEmpty(userModel.getDevicetype())) {
                //app登录：
                userright.setDevicetoken(userModel.getDevicetoken());
                userright.setDevicetype(userModel.getDevicetype());
                userrightDao.save(userright);
                monitorequipmenttypeModels = equipmentTypeMapper.getAllEquipmentType(userright.getHospitalcode());
                userid = "app"+":" + userid;
            }else {
                monitorequipmenttypeModels = equipmentTypeMapper.getAllEquipmentType(userright.getHospitalcode());
            }
            String token = TokenHelper.createToken(userid);
            redisTemplateUtil.boundValueOps(userid).set(token);
            Hospitalofreginfo one = hospitaDao.findOne(userright.getHospitalcode());
            loginResponseModel.setHospitalcode(userright.getHospitalcode());
            loginResponseModel.setToken(token);
            loginResponseModel.setUserid(userright.getUserid());
            loginResponseModel.setPwd(userright.getPwd());
            loginResponseModel.setPhonenum(userright.getPhonenum());
            //如果不是管理员不能看到设置警报菜单
//            if(!"admin".equals(userright.getUsertype())){
//                monitorequipmenttypeModels =
//                        monitorequipmenttypeModels.stream()
//                                .filter(item -> !"9".equals(item.getEquipmenttypeid())
//                                        | !"设置报警".equals(item.getEquipmenttypename())).collect(Collectors.toList());
//            }
            loginResponseModel.setHospitalEquipmentType(monitorequipmenttypeModels);
            loginResponseModel.setUsername(userright.getUsername());
            loginResponseModel.setUsertype(userright.getUsertype());
            loginResponseModel.setDevicetype(userright.getUsertype());
            loginResponseModel.setHospitalname(one.getHospitalname());

            apiResponse.setCode(ApiResponse.SUCCESS);
            apiResponse.setMessage("登录成功");
            LOGGER.info("安卓登录："+JsonUtil.toJson(loginResponseModel));
            apiResponse.setResult(loginResponseModel);
            return apiResponse;
        }catch(Exception e) {
            LOGGER.error("失败：" +e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<LoginResponseModel> userLoginByAndrio(UserModel userModel) {
        ApiResponse<LoginResponseModel> apiResponse = new ApiResponse<LoginResponseModel>();
        LoginResponseModel loginResponseModel = new LoginResponseModel();
        Userright userright = new Userright();
        try{
            userright = userInfoFromMapper.userlogin(userModel);
            if (StringUtils.isEmpty(userright)) {
                apiResponse.setMessage("用户名或密码错误");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            String userid = userright.getUserid();
            List<MonitorequipmenttypeModel> monitorequipmenttypeModels = new ArrayList<MonitorequipmenttypeModel>();
            if (!StringUtils.isEmpty(userModel.getDevicetype())) {
                //app登录：
                userright.setDevicetoken(userModel.getDevicetoken());
                userright.setDevicetype(userModel.getDevicetype());
                userrightDao.save(userright);
                monitorequipmenttypeModels = equipmentTypeMapper.getAllEquipmentType(userright.getHospitalcode());
                userid = "app"+":" + userid;
            }else {
                monitorequipmenttypeModels = equipmentTypeMapper.getAllEquipmentType(userright.getHospitalcode());
            }
            String token = TokenHelper.createToken(userid);
            redisTemplateUtil.boundValueOps(userid).set(token);
            Hospitalofreginfo one = hospitaDao.getOne(userright.getHospitalcode());
            loginResponseModel.setHospitalcode(userright.getHospitalcode());
            loginResponseModel.setToken(token);
            loginResponseModel.setUserid(userright.getUserid());
            loginResponseModel.setPwd(userright.getPwd());
            loginResponseModel.setPhonenum(userright.getPhonenum());
            loginResponseModel.setHospitalEquipmentType(monitorequipmenttypeModels);
            loginResponseModel.setUsername(userright.getUsername());
            loginResponseModel.setDevicetype(userright.getUsertype());
            loginResponseModel.setHospitalname(one.getHospitalname());
            if (!monitorequipmenttypeModels.isEmpty()) {
                MonitorequipmenttypeModel monitorequipmenttypeModel = monitorequipmenttypeModels.get(0);
                ApiResponse<Page<Monitorequipment>> equipmentCurrentDataPage = equipmentInfoService.getEquipmentCurrentDataPage(userright.getHospitalcode(), monitorequipmenttypeModel.getEquipmenttypeid(), 10, 1,null);
                loginResponseModel.setMonitorequipments(equipmentCurrentDataPage);
                ApiResponse<Monitorupsrecord> currentUps = equipmentInfoService.getCurrentUps(userright.getHospitalcode(), "6");
                loginResponseModel.setMonitorupsrecord(currentUps);
            }
            apiResponse.setCode(ApiResponse.SUCCESS);
            apiResponse.setMessage("登录成功");
            LOGGER.info("安卓登录："+JsonUtil.toJson(loginResponseModel));
            apiResponse.setResult(loginResponseModel);
            return apiResponse;
        }catch(Exception e) {
            LOGGER.error("失败：" +e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<LoginResponseModel> userLoginByAndrios(UserModel userModel) {
        ApiResponse<LoginResponseModel> apiResponse = new ApiResponse<LoginResponseModel>();
        LoginResponseModel loginResponseModel = new LoginResponseModel();
        Userright userright = new Userright();
        try{
            userright = userInfoFromMapper.userlogin(userModel);
            if (StringUtils.isEmpty(userright)) {
                apiResponse.setMessage("用户名或密码错误");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            String userid = userright.getUserid();
            List<MonitorequipmenttypeModel> monitorequipmenttypeModels = new ArrayList<MonitorequipmenttypeModel>();
            if (!StringUtils.isEmpty(userModel.getDevicetype())) {
                //app登录：
                userright.setDevicetoken(userModel.getDevicetoken());
                userright.setDevicetype(userModel.getDevicetype());
                userrightDao.save(userright);
                monitorequipmenttypeModels = equipmentTypeMapper.getAllEquipmentType(userright.getHospitalcode());
                userid = "app"+":" + userid;
            }else {
                monitorequipmenttypeModels = equipmentTypeMapper.getAllEquipmentType(userright.getHospitalcode());
            }
          //  String token = TokenHelper.createToken(userid);
         //   redisTemplateUtil.boundValueOps(userid).set(token);
            Hospitalofreginfo one = hospitaDao.getOne(userright.getHospitalcode());
            loginResponseModel.setHospitalcode(userright.getHospitalcode());
       //     loginResponseModel.setToken(token);
            loginResponseModel.setUserid(userright.getUserid());
            loginResponseModel.setPwd(userright.getPwd());
            loginResponseModel.setPhonenum(userright.getPhonenum());
            loginResponseModel.setHospitalEquipmentType(monitorequipmenttypeModels);
            loginResponseModel.setUsername(userright.getUsername());
            loginResponseModel.setHospitalname(one.getHospitalname());
            loginResponseModel.setDevicetype(userright.getUsertype());
            loginResponseModel.setUsertype(userright.getUsertype());
            if (!monitorequipmenttypeModels.isEmpty()) {
                MonitorequipmenttypeModel monitorequipmenttypeModel = monitorequipmenttypeModels.get(0);
                ApiResponse<Page<Monitorequipment>> equipmentCurrentDataPage = equipmentInfoService.getEquipmentCurrentDataPage(userright.getHospitalcode(), monitorequipmenttypeModel.getEquipmenttypeid(), 10, 1,null);
                loginResponseModel.setMonitorequipments(equipmentCurrentDataPage);
                ApiResponse<Monitorupsrecord> currentUps = equipmentInfoService.getCurrentUps(userright.getHospitalcode(), "6");
                loginResponseModel.setMonitorupsrecord(currentUps);
            }
            apiResponse.setCode(ApiResponse.SUCCESS);
            apiResponse.setMessage("登录成功");
            LOGGER.info("安卓登录："+JsonUtil.toJson(loginResponseModel));
            apiResponse.setResult(loginResponseModel);
            return apiResponse;
        }catch(Exception e) {
            LOGGER.error("失败：" +e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }


    @Override
    public ApiResponse<String> getCode(String phonenum) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        try{
            String code = String.valueOf((int) (Math.random() * 9000) + 1000);
            Map<String,String> map = new HashMap<>();
            if (StringUtils.isEmpty(phonenum)){
                apiResponse.setMessage("手机号为空");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            map.put("phonenum",phonenum);
            map.put("code",code);
            String s = HttpUtil.get("http://39.104.102.191:8093/code", map);
            SendSmsResponse sendSmsResponse = JsonUtil.toBean(s, SendSmsResponse.class);

            LOGGER.info("调用短信接口返回值："+ JsonUtil.toJson(sendSmsResponse)+"参数：电话："+phonenum+"code:"+code);

            redisTemplateUtil.boundValueOps(code).set(code,2, TimeUnit.MINUTES);
            return apiResponse;
        }catch (Exception e){
            LOGGER.error("获取短信验证码失败：" +e);
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }


    @Override
    public ApiResponse<String> updatePhone(UserModel userModel) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        try{
            //验证短信验证码
            String code = userModel.getCode();
            String codeValide = (String) redisTemplateUtil.boundValueOps(code).get();
            if (StringUtils.isEmpty(codeValide)) {
                apiResponse.setMessage("短信验证码过期，请重新获取验证码");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            if (!code.equals(codeValide)) {
                apiResponse.setMessage("验证码错误");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            //tode
            String phonenum = userModel.getPhonenum();
            String userid = userModel.getUserid();
            userrightDao.updatePhonenum(phonenum, userid);
            Userright one = userrightDao.findOne(userid);
            //同步缓存
            List<Userright> list = clientInfoMapper.selectUserInfoByHospitalcode(one.getHospitalcode());
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            objectObjectObjectHashOperations.delete("hospital:phonenum",one.getHospitalcode());
            if (CollectionUtils.isNotEmpty(list)){
                objectObjectObjectHashOperations.put("hospital:phonenum",one.getHospitalcode(),JsonUtil.toJson(list));
            }
            apiResponse.setCode(ApiResponse.SUCCESS);
            apiResponse.setMessage("修改成功");
            return apiResponse;
        }catch(Exception e) {
            LOGGER.error("失败：" +e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<String> updatePwd(UserModel userModel) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        try {
            //验证短信验证码
            String code = userModel.getCode();
            String codeValide = (String) redisTemplateUtil.boundValueOps(code).get();
            if (StringUtils.isEmpty(codeValide)) {
                apiResponse.setMessage("短信验证码过期，请重新获取验证码");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            if (!code.equals(codeValide)) {
                apiResponse.setMessage("验证码错误");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            String pwd = userModel.getPwd();
            String userid = userModel.getUserid();
            userrightDao.updatePwd(pwd,userid);
            apiResponse.setCode(ApiResponse.SUCCESS);
            apiResponse.setMessage("修改成功");
            return apiResponse;
        }catch(Exception e) {
            LOGGER.error("失败：" +e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<String> loginOut(String token ) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        try{
            //当前位置未做清空好像

            String userid = TokenHelper.getUserID(token);
            redisTemplateUtil.delete(userid);
            //清除devicetoken
            if (userid.indexOf(":")> -1){
                //APP退出  需要清除device token  devicetype
                String substring = userid.substring(userid.indexOf(":") + 1);
                LOGGER.info("APP退出登录userid:"+substring);
                userrightDao.updateDevice(substring);
            }
            apiResponse.setMessage("退出登录成功");
            //清除医院信息

            apiResponse.setCode(ApiResponse.SUCCESS);
            return apiResponse;
        }catch (Exception e){
            LOGGER.error("失败：" +e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }
}
