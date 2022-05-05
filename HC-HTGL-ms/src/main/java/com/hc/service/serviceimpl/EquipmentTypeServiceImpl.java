package com.hc.service.serviceimpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.hc.config.RedisTemplateUtil;
import com.hc.infrastructure.dao.HospitalEquipmentDao;
import com.hc.infrastructure.dao.HospitalofreginfoDao;
import com.hc.infrastructure.dao.MonitorEquipmentWarningTimeDao;
import com.hc.po.*;
import com.hc.mapper.laboratoryFrom.HospitalEquipmentMapper;
import com.hc.model.MapperModel.PageUserModel;
import com.hc.model.RequestModel.EquipmentTypeInfoModel;
import com.hc.model.RequestModel.WorkTimeBlockModel;
import com.hc.model.ResponseModel.HospitalEquipmentTypeInfoModel;
import com.hc.service.EquipmentTypeService;
import com.hc.service.UpdateRecordService;
import com.hc.units.ApiResponse;
import com.hc.units.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

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
    @Autowired
    private MonitorEquipmentWarningTimeDao monitorEquipmentWarningTimeDao;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

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
        hospitalequiment.setAlwayalarm(equipmentTypeInfoModel.getAlwayalarm());
        try {
            //判断当前医院是否存在输入设备类型
            Integer M = hospitalEquipmentMapper.isEquipmenttype(hospitalcode, equipmenttypeid);
            if (M > 0) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("当前医院存在改设备类型,请勿重复添加");
                return apiResponse;
            }
            //插入设备类型时间段
            WorkTimeBlockModel[] workTimeBlock = equipmentTypeInfoModel.getWorkTimeBlock();
            List<MonitorEquipmentWarningTime> monitorEquipmentWarningTimeList = new ArrayList<MonitorEquipmentWarningTime>();
            if(workTimeBlock != null){
                for(int i=0;i<workTimeBlock.length;i++){
                    WorkTimeBlockModel workTime = workTimeBlock[i];
                    if(workTime != null){
                        MonitorEquipmentWarningTime monitorEquipmentWarningTime = new MonitorEquipmentWarningTime();
                        monitorEquipmentWarningTime.setBegintime(workTime.getBegintime());
                        monitorEquipmentWarningTime.setEndtime(workTime.getEndtime());
                        monitorEquipmentWarningTime.setEquipmentcategory("TYPE");
                        monitorEquipmentWarningTime.setHospitalcode(hospitalcode);
                        monitorEquipmentWarningTime.setEquipmentid(equipmenttypeid);
                        monitorEquipmentWarningTimeList.add(monitorEquipmentWarningTime);
                    }
                }
            }
            monitorEquipmentWarningTimeDao.save(monitorEquipmentWarningTimeList);
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

            //警报时间段
            WorkTimeBlockModel[] workTimeBlock = equipmentTypeInfoModel.getWorkTimeBlock();
            List<MonitorEquipmentWarningTime> monitorEquipmentWarningTimeList = new ArrayList<MonitorEquipmentWarningTime>();
            if(workTimeBlock != null){
                for(int i = 0;i<workTimeBlock.length;i++){
                    WorkTimeBlockModel workTimeBlockModel = workTimeBlock[i];
                    if(workTimeBlockModel != null){
                        Integer timeblockid = workTimeBlockModel.getTimeblockid();
                        MonitorEquipmentWarningTime monitorEquipmentWarningTime = new MonitorEquipmentWarningTime();
                        if(timeblockid != null){
                            monitorEquipmentWarningTime.setTimeblockid(timeblockid);
                        }
                        monitorEquipmentWarningTime.setBegintime(workTimeBlockModel.getBegintime());
                        monitorEquipmentWarningTime.setEndtime(workTimeBlockModel.getEndtime());
                        monitorEquipmentWarningTime.setEquipmentid(equipmenttypeid);
                        monitorEquipmentWarningTime.setEquipmentcategory("TYPE");
                        monitorEquipmentWarningTime.setHospitalcode(hospitalcode);
                        monitorEquipmentWarningTimeList.add(monitorEquipmentWarningTime);
                    }
                }
                if(!monitorEquipmentWarningTimeList.isEmpty()){
                    monitorEquipmentWarningTimeDao.save(monitorEquipmentWarningTimeList);
                }
                //移除的时间警报数据
                WorkTimeBlockModel[] deleteWarningTimeBlock = equipmentTypeInfoModel.getDeleteWarningTimeBlock();
                if(deleteWarningTimeBlock != null){
                    Set<MonitorEquipmentWarningTime> delWorkTimeBlockModels = new HashSet<MonitorEquipmentWarningTime>();
                    for(int i=0;i<deleteWarningTimeBlock.length;i++){
                        WorkTimeBlockModel del = deleteWarningTimeBlock[i];
                        if(del != null){
                            MonitorEquipmentWarningTime monitorEquipmentWarningTime = new MonitorEquipmentWarningTime();
                            Integer timeblockid = del.getTimeblockid();
                            boolean exists = monitorEquipmentWarningTimeDao.exists(timeblockid);
                            if(exists){
                                monitorEquipmentWarningTime.setTimeblockid(timeblockid);
                                delWorkTimeBlockModels.add(monitorEquipmentWarningTime);
                            }
                        }
                    }

                    if(!delWorkTimeBlockModels.isEmpty()){
                        monitorEquipmentWarningTimeDao.delete(delWorkTimeBlockModels);
                    }
                }
            }

            //更新缓存
            HospitalEquipmentTypeInfoModel hospitalEquipment = hospitalEquipmentMapper
                    .selectEquipmentTypeByHospitalcodeAndEquipmenttypeid(equipmentTypeInfoModel.getHospitalcode(),
                            equipmentTypeInfoModel.getEquipmenttypeid());
            if(hospitalEquipment != null){
                MonitorEquipmentWarningTime monitorEquipmentWarningTime = new MonitorEquipmentWarningTime();
                monitorEquipmentWarningTime.setEquipmentid(hospitalEquipment.getEquipmenttypeid());
                monitorEquipmentWarningTime.setEquipmentcategory("TYPE");
                monitorEquipmentWarningTime.setHospitalcode(hospitalEquipment.getHospitalcode());
                Example<MonitorEquipmentWarningTime> timeExample = Example.of(monitorEquipmentWarningTime);
                List<MonitorEquipmentWarningTime> warningTimeDaoAll = monitorEquipmentWarningTimeDao.findAll(timeExample);
                hospitalEquipment.setWarningTimeList(warningTimeDaoAll);
                hospitalEquipment.setAlwayalarm(equipmentTypeInfoModel.getAlwayalarm());
                //同步探头名称到缓存
                String key = hospitalEquipment.getEquipmenttypeid()+"@"+ hospitalEquipment.getHospitalcode();
                Object o = redisTemplateUtil.boundHashOps("hospital:equipmenttype")
                        .get(key);
                //存在
                String o1 = (String) o;
                HospitalEquipmentTypeInfoModel monitorinstrumentObj = JsonUtil.toBean(o1, HospitalEquipmentTypeInfoModel.class);
                if (monitorinstrumentObj != null){
                    redisTemplateUtil.boundHashOps("hospital:equipmenttype").put(key,JsonUtil.toJson(hospitalEquipment));
                }
            }

            Hospitalofreginfo one = hospitalofreginfoDao.getOne(hospitalcode1);
            updateRecordService.updateEquipmentType(one.getHospitalname(),usernames,info,equipmentTypeInfoModel,"0","1");
            hospitalEquipmentDao.upateByEquipmenttypeid(isvisible,timeout,timeouttime,
                    equipmentTypeInfoModel.getAlwayalarm(),hospitalequimentKey);

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
            for(HospitalEquipmentTypeInfoModel hospitalEquipmentTypeInfoModel : hospitalEquipmentTypeInfoModels){
                MonitorEquipmentWarningTime monitorEquipmentWarningTime = new MonitorEquipmentWarningTime();
                monitorEquipmentWarningTime.setEquipmentid(hospitalEquipmentTypeInfoModel.getEquipmenttypeid());
                monitorEquipmentWarningTime.setEquipmentcategory("TYPE");
                monitorEquipmentWarningTime.setHospitalcode(hospitalEquipmentTypeInfoModel.getHospitalcode());
                Example<MonitorEquipmentWarningTime> timeExample = Example.of(monitorEquipmentWarningTime);
                List<MonitorEquipmentWarningTime> warningTimeDaoAll = monitorEquipmentWarningTimeDao.findAll(timeExample);
                hospitalEquipmentTypeInfoModel.setWarningTimeList(warningTimeDaoAll);
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
