package com.hc.service.serviceimpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.hc.mapper.UserEquipmentRightDao;
import com.hc.entity.Userequipmentright;
import com.hc.mapper.laboratoryFrom.UserAuthorInfoMapper;
import com.hc.model.RequestModel.UserauthorInfoModel;
import com.hc.model.ResponseModel.UserAuthInfoModel;
import com.hc.service.UserAuthoSetService;
import com.hc.units.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 16956 on 2018-08-06.
 */
@Service
public class UserAuthoSetServiceImpl implements UserAuthoSetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    private UserEquipmentRightDao userEquipmentRightDao;
    @Autowired
    private UserAuthorInfoMapper userAuthorInfoMapper;
    @Override
    public ApiResponse<Page<UserAuthInfoModel>> showUserAuth(Integer pagesize, Integer pagenum) {
        ApiResponse<Page<UserAuthInfoModel>> apiResponse = new ApiResponse<Page<UserAuthInfoModel>>();
        List<UserAuthInfoModel> userAuthInfoModels = new ArrayList<UserAuthInfoModel>();
        try{
            Integer start = (pagenum-1) * pagesize;
            Integer end = pagesize;
            PageRowBounds page = new PageRowBounds(start,end);
            userAuthInfoModels = userAuthorInfoMapper.selectUserAuthorPage(page);
            if (CollectionUtils.isEmpty(userAuthInfoModels)){
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("不存在人员信息");
                return apiResponse;
            }
            PageInfo<UserAuthInfoModel> pageInfo = new PageInfo<UserAuthInfoModel>(userAuthInfoModels);
            apiResponse.setPage(pageInfo);
            return apiResponse;
        }catch(Exception e){
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<String> updateAuthor(UserauthorInfoModel userauthorInfoModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        String type = userauthorInfoModel.getType();
        String userid = userauthorInfoModel.getUserid();
        String equipmenttypeid = userauthorInfoModel.getEquipmenttypeid();
        try{
            switch (type){
                case "0":
                    //删除权
                    userEquipmentRightDao.deleteByEquipmentitem(userid,equipmenttypeid);
                    break;
                case "1":
                    //添加权限
                    Userequipmentright userequipmentright = new Userequipmentright();
                    userequipmentright.setEquipmentitem(equipmenttypeid);
                    userequipmentright.setUserid(userid);
                    userEquipmentRightDao.save(userequipmentright);
                    break;
            }
            return apiResponse;
        }catch (Exception e){
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }
}
