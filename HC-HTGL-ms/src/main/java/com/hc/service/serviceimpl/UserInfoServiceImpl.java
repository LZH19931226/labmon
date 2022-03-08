package com.hc.service.serviceimpl;

import com.hc.config.RedisTemplateUtil;
import com.hc.dao.UserScheduLingDao;
import com.hc.entity.UserScheduLing;
import com.hc.entity.Userback;
import com.hc.mapper.laboratoryFrom.UserAuthorInfoMapper;
import com.hc.mapper.laboratoryFrom.UserInfoMapper;
import com.hc.model.RequestModel.UserScheduLingPostModel;
import com.hc.my.common.core.util.DateUtils;
import com.hc.service.UserInfoService;
import com.hc.units.ApiResponse;
import com.hc.units.TokenHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 16956 on 2018-08-05.
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private UserAuthorInfoMapper userAuthorInfoMapper;
    @Autowired
    private UserScheduLingDao userScheduLingDao;


    @Override
    public ApiResponse<String> userLogin(Userback userback) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        Userback userback1;
        try{
            userback1 = userInfoMapper.userLogin(userback);
            if (StringUtils.isEmpty(userback1)){
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("登录失败，用户名或密码错误");
            }
            String username = userback.getUsername();
            //登录成功
            String token = TokenHelper.createToken(username+username);
            redisTemplateUtil.boundValueOps(username+username).set(token,1, TimeUnit.HOURS);
            apiResponse.setResult(token);
            return apiResponse;
        }catch(Exception e){
            LOGGER.error("失败：" +e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }

    }

    @Override
    public ApiResponse<String> updatePassword(Userback userback) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        Userback  userback1 = userInfoMapper.userLogin(userback);
        if (null == userback1){
            apiResponse.setCode(ApiResponse.FAILED);
            apiResponse.setMessage("用户名或者密码错误");
            return apiResponse;
        }
        userback1.setPwd(userback.getNewpwd());
        userInfoMapper.updatePwd(userback1);

        return apiResponse;
    }

    @Override
    @Transactional
    public ApiResponse<String> addusersc(UserScheduLingPostModel userScheduLingPostModel) {
        List<UserScheduLing> userScheduLings = userScheduLingPostModel.getUserScheduLings();
        Date starttime = userScheduLingPostModel.getStarttime();
        String createuser = userScheduLingPostModel.getCreateuser();
        Date endtime = userScheduLingPostModel.getEndtime();
        String hospitalcode = userScheduLingPostModel.getHospitalcode();
        userScheduLingDao.deleteStHos(hospitalcode,DateUtils.paseDatetime(starttime),DateUtils.paseDatetime(endtime));
        if (CollectionUtils.isNotEmpty(userScheduLings)){
            userScheduLings.forEach(s->{
                s.setHospitalcode(hospitalcode);
                s.setCreatetime(new Date());
                s.setCreateuser(createuser);
            });
            userScheduLingDao.save(userScheduLings);
        }
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<List<UserScheduLing>> searchScByHosMon(String hosId, String month) {
        ApiResponse<List<UserScheduLing>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userAuthorInfoMapper.searchScByHosMon(hosId,month));
        return apiResponse;
    }

    @Override
    public ApiResponse<List<UserScheduLing>> searchScByHosMonSection(String hosId, String startMonth, String endMonth) {
        ApiResponse<List<UserScheduLing>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userAuthorInfoMapper.searchScByHosMonSection(hosId,startMonth,endMonth));
        return apiResponse;
    }
}
