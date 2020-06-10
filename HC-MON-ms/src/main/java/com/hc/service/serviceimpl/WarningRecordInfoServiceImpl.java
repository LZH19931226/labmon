package com.hc.service.serviceimpl;

import com.hc.dao.WarningRecordInfoDao;
import com.hc.entity.WarningRecordInfo;
import com.hc.service.WarningRecordInfoService;
import com.hc.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author LiuZhiHao
 * @date 2020/6/8 14:46
 * 描述:
 **/
@Service
public class WarningRecordInfoServiceImpl implements WarningRecordInfoService {

    @Autowired
    private WarningRecordInfoDao wreDao;


    @Override
    public ApiResponse<String> instwarningrecordinfo(WarningRecordInfo warningrecordinfo) {
        try {
            int id = warningrecordinfo.getId();
            if (id != 0) {
                warningrecordinfo.setUpdatetime(new Date());
                wreDao.updateWarningRecordInfo(warningrecordinfo.getInfo(), warningrecordinfo.getUpdateuser(), warningrecordinfo.getId());
                return ApiResponse.updateSuccess();
            } else {
                warningrecordinfo.setCreatetime(new Date());
            }
            wreDao.save(warningrecordinfo);
        } catch (Exception e) {
            return ApiResponse.fail(e.getMessage());
        }
        return ApiResponse.saveSuccess();
    }


}
