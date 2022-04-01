package com.hc.service.serviceimpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.hc.config.RedisTemplateUtil;
import com.hc.dao.UserEquipmentRightDao;
import com.hc.dao.UserRightDao;
import com.hc.entity.Userright;
import com.hc.mapper.laboratoryFrom.ClientInfoMapper;
import com.hc.mapper.laboratoryFrom.OperationlogdetaiMapper;
import com.hc.model.MapperModel.PageUserModel;
import com.hc.model.ResponseModel.ClientInfoModel;
import com.hc.service.ClientInfoService;
import com.hc.service.UpdateRecordService;
import com.hc.units.ApiResponse;
import com.hc.units.JsonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * Created by 16956 on 2018-08-06.
 */
@Service
public class ClientInfoServiceImpl implements ClientInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInfoServiceImpl.class);
    @Autowired
    private UserRightDao userRightDao;
    @Autowired
    private ClientInfoMapper clientInfoMapper;
    @Autowired
    private UserEquipmentRightDao userEquipmentRightDao;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private UpdateRecordService updateRecordService;
    @Autowired
    private OperationlogdetaiMapper operationlogdetaiMapper;
    @Override
    public ApiResponse<Userright> addUser(Userright userright) {
        ApiResponse<Userright> apiResponse = new ApiResponse<>();
        try{
            //相同用户名或者id 验证
            Userright userright1 = clientInfoMapper.selectUserByUser(userright);

            if (!StringUtils.isEmpty(userright1)) {
                apiResponse.setMessage("存在相同用户名或ID，请重新输入");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            userright.setUserid(UUID.randomUUID().toString().replaceAll("-", ""));
            userright1 = userRightDao.save(userright);
            String usernames = userright.getUsernames();
            Userright userright2 = new Userright();
            //查询医院名称
            String hospitalName = operationlogdetaiMapper.getHospitalName(userright1.getUserid());
            updateRecordService.updateUser(hospitalName,usernames,userright2,userright1,"0","0");
            apiResponse.setResult(userright1);
            //将当前医院所有人员同步进redis
            List<Userright> list = clientInfoMapper.selectUserInfoByHospitalcode(userright1.getHospitalcode());
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            objectObjectObjectHashOperations.put("hospital:phonenum",userright1.getHospitalcode(),JsonUtil.toJson(list));
            return apiResponse;
        }catch (Exception e){
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<String> deleteUser(Userright userright) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        try{
            //删除用户对应的设备
            userEquipmentRightDao.deleteAllByUserid(userright.getUserid());
            Userright one = userRightDao.getOne(userright.getUserid());
            String hospitalName = operationlogdetaiMapper.getHospitalName(userright.getUserid());
            String usernames = userright.getUsernames();
            Userright userright1 = new Userright();
            updateRecordService.updateUser(hospitalName,usernames,one,userright1,"0","2");
            //珊瑚用户
            userRightDao.delete(userright.getUserid());
            List<Userright> list = clientInfoMapper.selectUserInfoByHospitalcode(userright.getHospitalcode());
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            objectObjectObjectHashOperations.delete("hospital:phonenum",userright.getHospitalcode());
            if (CollectionUtils.isNotEmpty(list)){
                objectObjectObjectHashOperations.put("hospital:phonenum",userright.getHospitalcode(),JsonUtil.toJson(list));
            }

            return apiResponse;

        }catch (Exception e){
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<Userright> updateUser(Userright userright) {
        ApiResponse<Userright> apiResponse = new ApiResponse<Userright>();
        try{
            String userid = userright.getUserid();
          //  Userright one1 = new Userright();
            Userright one = clientInfoMapper.selectUserByUserId(userright.getUserid());
            //one1 = one;
            Userright userright1 = userRightDao.save(userright);

            String hospitalName = operationlogdetaiMapper.getHospitalName(userid);
            String usernames = userright.getUsernames();
            LOGGER.info("原始数据："+JsonUtil.toJson(one));
            LOGGER.info("新数据："+JsonUtil.toJson(userright1));
            updateRecordService.updateUser(hospitalName,usernames,one,userright1,"0","1");
            List<Userright> list = clientInfoMapper.selectUserInfoByHospitalcode(userright.getHospitalcode());
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            objectObjectObjectHashOperations.put("hospital:phonenum",userright.getHospitalcode(), JsonUtil.toJson(list));
            apiResponse.setResult(userright1);
            return apiResponse;
        }catch (Exception e){
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常" );
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<Page<ClientInfoModel>> selectUserInfoPage(String hospitalcode, String fuzzy, Integer pagesize, Integer pagenum,String setterWarningUsername) {
        ApiResponse<Page<ClientInfoModel>> apiResponse = new ApiResponse<Page<ClientInfoModel>>();
        PageUserModel pageUserModel = new PageUserModel();
        try{
            Integer start = (pagenum-1) * pagesize;
            Integer end = pagesize;
            PageRowBounds page = new PageRowBounds(start,end);
            if (!StringUtils.isEmpty(fuzzy)){
                fuzzy = "%" + fuzzy + "%";
            }
            pageUserModel.setHospitalcode(hospitalcode);
            pageUserModel.setFuzzy(fuzzy);
            pageUserModel.setSetterWarningUsername(setterWarningUsername);
            List<ClientInfoModel> clientInfoMapperList = clientInfoMapper.selectUserInfoPage(page, pageUserModel);
            if (CollectionUtils.isEmpty(clientInfoMapperList)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("无用户账户信息");
                return apiResponse;
            }
            PageInfo<ClientInfoModel> pageInfo = new PageInfo<ClientInfoModel>(clientInfoMapperList);
            apiResponse.setPage(pageInfo);
            return apiResponse;
        }catch(Exception e){
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }
}

