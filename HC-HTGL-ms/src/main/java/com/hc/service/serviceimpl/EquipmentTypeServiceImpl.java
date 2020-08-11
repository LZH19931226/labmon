package com.hc.service.serviceimpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.hc.dao.HospitalEquipmentDao;
import com.hc.dao.HospitalofreginfoDao;
import com.hc.entity.Hospitalequiment;
import com.hc.entity.HospitalequimentKey;
import com.hc.entity.Hospitalofreginfo;
import com.hc.entity.Monitorequipmenttype;
import com.hc.mapper.laboratoryFrom.HospitalEquipmentMapper;
import com.hc.model.MapperModel.PageUserModel;
import com.hc.model.RequestModel.EquipmentTypeInfoModel;
import com.hc.model.ResponseModel.HospitalEquipmentTypeInfoModel;
import com.hc.service.EquipmentTypeService;
import com.hc.service.UpdateRecordService;
import com.hc.units.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by 16956 on 2018-08-06.
 */
@Service
public class EquipmentTypeServiceImpl implements EquipmentTypeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentTypeServiceImpl.class);
    @Autowired
    private HospitalEquipmentDao hospitalEquipmentDao;
    @Autowired
    private HospitalEquipmentMapper hospitalEquipmentMapper;


    @Autowired
    private UpdateRecordService updateRecordService;

    @Autowired
    private HospitalofreginfoDao hospitalofreginfoDao;

    /**
     * 添加设备类型  ： 未开放直接添加设备和探头功能，未来可做添加，当前环境下不做该功能
     *
     * @param equipmentTypeInfoModel
     * @return
     */
    @Override
    public ApiResponse<String> addEquipmentType(EquipmentTypeInfoModel equipmentTypeInfoModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        Hospitalequiment hospitalequiment = new Hospitalequiment();
        String hospitalcode = equipmentTypeInfoModel.getHospitalcode();
        String equipmenttypeid = equipmentTypeInfoModel.getEquipmenttypeid();
        hospitalequiment.setHospitalequimentKey(new HospitalequimentKey(equipmenttypeid, hospitalcode));
        hospitalequiment.setIsvisible(equipmentTypeInfoModel.getIsvisible());
        String timeout = equipmentTypeInfoModel.getTimeout();
        Integer timeouttime = equipmentTypeInfoModel.getTimeouttime();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(timeout)){
            hospitalequiment.setTimeout(timeout);
            hospitalequiment.setTimeouttime(timeouttime);
        }
        hospitalequiment.setOrderno(new Integer(equipmentTypeInfoModel.getEquipmenttypeid()));
        try {
            //判断当前医院是否存在输入设备类型
            Integer M = hospitalEquipmentMapper.isEquipmenttype(hospitalcode, equipmenttypeid);
            if (M > 0) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("当前医院存在改设备类型,请勿重复添加");
                return apiResponse;
            }
            //插入设备类型
            String usernames = equipmentTypeInfoModel.getUsernames();
            String hospitalcode1 = equipmentTypeInfoModel.getHospitalcode();
            EquipmentTypeInfoModel equipmentTypeInfoModel1 = new EquipmentTypeInfoModel();
            Hospitalofreginfo one = hospitalofreginfoDao.getOne(hospitalcode1);
            updateRecordService.updateEquipmentType(one.getHospitalname(),usernames,equipmentTypeInfoModel1,equipmentTypeInfoModel,"0","0");
            hospitalEquipmentDao.save(hospitalequiment);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<String> deleteEquipmentType(EquipmentTypeInfoModel equipmentTypeInfoModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        String hospitalcode = equipmentTypeInfoModel.getHospitalcode();
        String equipmenttypeid = equipmentTypeInfoModel.getEquipmenttypeid();
        HospitalequimentKey hospitalequimentKey = new HospitalequimentKey();
        hospitalequimentKey.setEquipmenttypeid(equipmenttypeid);
        hospitalequimentKey.setHospitalcode(hospitalcode);
        try {
            Integer K = hospitalEquipmentMapper.selectEquipmentIs(equipmentTypeInfoModel);
            if (K == 0) {
                //不存在设备，可删除
                String usernames = equipmentTypeInfoModel.getUsernames();
                String hospitalcode1 = equipmentTypeInfoModel.getHospitalcode();
                EquipmentTypeInfoModel equipmentTypeInfoModel1 = new EquipmentTypeInfoModel();
                Hospitalofreginfo one = hospitalofreginfoDao.getOne(hospitalcode1);
                updateRecordService.updateEquipmentType(one.getHospitalname(),usernames,equipmentTypeInfoModel,equipmentTypeInfoModel1,"0","2");
                hospitalEquipmentMapper.deleteEquipmenttype(equipmentTypeInfoModel);
            } else {
                apiResponse.setMessage("当前设备类型存在设备，无法删除");
                apiResponse.setCode(ApiResponse.FAILED);
            }
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<String> updateEquipmentType(EquipmentTypeInfoModel equipmentTypeInfoModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        String isvisible = equipmentTypeInfoModel.getIsvisible();
        String hospitalcode = equipmentTypeInfoModel.getHospitalcode();
        String equipmenttypeid = equipmentTypeInfoModel.getEquipmenttypeid();
        HospitalequimentKey hospitalequimentKey = new HospitalequimentKey();
        hospitalequimentKey.setEquipmenttypeid(equipmenttypeid);
        hospitalequimentKey.setHospitalcode(hospitalcode);
        String timeout = equipmentTypeInfoModel.getTimeout();
        Integer timeouttime = equipmentTypeInfoModel.getTimeouttime();

        try {
            EquipmentTypeInfoModel info = hospitalEquipmentMapper.getInfo(hospitalequimentKey.getHospitalcode(), hospitalequimentKey.getEquipmenttypeid());
            String usernames = equipmentTypeInfoModel.getUsernames();
            String hospitalcode1 = equipmentTypeInfoModel.getHospitalcode();

            Hospitalofreginfo one = hospitalofreginfoDao.getOne(hospitalcode1);
            updateRecordService.updateEquipmentType(one.getHospitalname(),usernames,info,equipmentTypeInfoModel,"0","1");
            hospitalEquipmentDao.upateByEquipmenttypeid(isvisible,timeout,timeouttime, hospitalequimentKey);

            return apiResponse;

        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<List<Monitorequipmenttype>> selectHospitalEquipment(String hospitalcode) {
        ApiResponse<List<Monitorequipmenttype>> apiResponse = new ApiResponse<List<Monitorequipmenttype>>();

            List<Monitorequipmenttype> list = hospitalEquipmentMapper.selectEquipmentTypeByCodes(hospitalcode);
            if (CollectionUtils.isEmpty(list)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("当前医院未添加设备");
                return apiResponse;
            }
            apiResponse.setResult(list);
            return apiResponse;

    }

    @Override
    public ApiResponse<List<Monitorequipmenttype>> selectEquipmentType() {
        ApiResponse<List<Monitorequipmenttype>> apiResponse = new ApiResponse<List<Monitorequipmenttype>>();

            List<Monitorequipmenttype> list = hospitalEquipmentMapper.selectAllEquipmetTypeInfo();
            if (CollectionUtils.isEmpty(list)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("当前医院未添加设备");
                return apiResponse;
            }
            apiResponse.setResult(list);
            return apiResponse;

    }

    @Override
    public ApiResponse<Page<HospitalEquipmentTypeInfoModel>> showAllHospitalEquipmentTypePage(String fuzzy, String hospitalcode, Integer pagesize, Integer pagenum) {
        ApiResponse<Page<HospitalEquipmentTypeInfoModel>> apiResponse = new ApiResponse<Page<HospitalEquipmentTypeInfoModel>>();
        PageUserModel pageUserModel = new PageUserModel();
        try {
            Integer start = (pagenum - 1) * pagesize;
            Integer end = pagesize;
            PageRowBounds page = new PageRowBounds(start, end);
            pageUserModel.setHospitalcode(hospitalcode);
            if (!StringUtils.isEmpty(fuzzy)) {
                fuzzy = "%" + fuzzy + "%";
            }
            pageUserModel.setFuzzy(fuzzy);
            List<HospitalEquipmentTypeInfoModel> hospitalEquipmentTypeInfoModels = hospitalEquipmentMapper.selectAllEquipmentPage(page, pageUserModel);
            if (CollectionUtils.isEmpty(hospitalEquipmentTypeInfoModels)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("无设备信息");
                return apiResponse;
            }
            PageInfo<HospitalEquipmentTypeInfoModel> pageInfo = new PageInfo<HospitalEquipmentTypeInfoModel>(hospitalEquipmentTypeInfoModels);
            apiResponse.setPage(pageInfo);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

}
