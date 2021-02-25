package com.hc.service.serviceimpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.hc.config.RedisTemplateUtil;
import com.hc.dao.InstrumentParamConfigDao;
import com.hc.dao.MonitorEquipmentDao;
import com.hc.dao.MonitorInstrumentDao;
import com.hc.dao.MonitorInstrumentTypeDao;
import com.hc.entity.Instrumentparamconfig;
import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorinstrument;
import com.hc.entity.Monitorinstrumenttype;
import com.hc.mapper.laboratoryFrom.InstrumentMonitorInfoMapper;
import com.hc.mapper.laboratoryFrom.MonitorEquipmentMapper;
import com.hc.mapper.laboratoryFrom.MonitorInstrumentMapper;
import com.hc.model.MapperModel.PageUserModel;
import com.hc.model.RequestModel.EquipmentInfoModel;
import com.hc.model.RequestModel.WiredInstrumentModel;
import com.hc.model.ResponseModel.AllInstrumentInfoModel;
import com.hc.model.ResponseModel.MonitorEquipmentInfoModel;
import com.hc.my.common.core.bean.InstrumentMonitorInfoModel;
import com.hc.service.MonitorEquipmentService;
import com.hc.service.UpdateRecordService;
import com.hc.units.ApiResponse;
import com.hc.units.JsonUtil;
import com.hc.units.MtUnConnectedSensorFilter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

;

/**
 * Created by 16956 on 2018-08-07.
 */
@Service
public class MonitroEquipmentServiceImpl implements MonitorEquipmentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitroEquipmentServiceImpl.class);
    @Autowired
    private MonitorEquipmentDao monitorEquipmentDao;  //设备dao
    @Autowired
    private MonitorInstrumentDao monitorInstrumentDao; //探头配置dao
    @Autowired
    private InstrumentMonitorInfoMapper instrumentMonitorInfoMapper; //报警探头类型
    @Autowired
    private InstrumentParamConfigDao instrumentParamConfigDao;  //探头参数Daao
    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;//  查询当前探头sn是否重复
    @Autowired
    private MonitorEquipmentMapper monitorEquipmentMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private MonitorInstrumentTypeDao monitorInstrumentTypeDao;
    @Autowired
    private UpdateRecordService updateRecordService;

    @Override
    public ApiResponse<String> addMonitorEquipment(EquipmentInfoModel equipmentInfoModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        Monitorequipment monitorequipment = new Monitorequipment();
        String hospitalcode = equipmentInfoModel.getHospitalcode();
        String equipmenttypeid = equipmentInfoModel.getEquipmenttypeid();
        String equipmentname = equipmentInfoModel.getEquipmentname();
        String equipmentbrand = equipmentInfoModel.getEquipmentbrand();
        boolean clientvisible = equipmentInfoModel.isClientvisible();
        Integer instrumenttypeid = equipmentInfoModel.getInstrumenttypeid();
        String instrumentno = equipmentInfoModel.getInstrumentno();
        if (instrumenttypeid == 12) {
            equipmentbrand = "COOK";
        } else if (instrumenttypeid == 13) {
            equipmentbrand = "C60";
        } else if (instrumenttypeid == 14) {
            equipmentbrand = "G185";
        }
        //需要添加探头
        if (StringUtils.isEmpty(equipmentInfoModel.getChannel())) {
            Integer J = monitorInstrumentMapper.isExist(equipmentInfoModel.getSn());
            if (J > 0) {
                apiResponse.setMessage("添加设备失败,sn已被占用");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
        }
        //添加设备  判断有无探头
        monitorequipment.setEquipmentbrand(equipmentbrand);
        monitorequipment.setHospitalcode(hospitalcode);
        monitorequipment.setClientvisible(clientvisible);
        monitorequipment.setEquipmentname(equipmentname);
        monitorequipment.setEquipmenttypeid(equipmenttypeid);
        monitorequipment.setEquipmentno(UUID.randomUUID().toString().replaceAll("-", ""));
        monitorequipment = monitorEquipmentDao.save(monitorequipment);
        //从后台解析服务中获取的sn
        try {
            if (StringUtils.isNotEmpty(instrumentno)) {
                //将探头和设备绑定
                Monitorinstrument monitorinstrument = new Monitorinstrument();
                monitorinstrument = monitorInstrumentMapper.selectMonInfoBySn(instrumentno);
                if (!ObjectUtils.isEmpty(monitorinstrument)) {
                    monitorinstrument.setEquipmentno(monitorequipment.getEquipmentno());
                    monitorinstrument.setInstrumentname(monitorequipment.getEquipmentname() + "探头");
                    monitorinstrument = monitorInstrumentDao.save(monitorinstrument);
                    HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
                    if (StringUtils.isNotEmpty(monitorinstrument.getChannel())) {
                        //同步缓存
                        objectObjectObjectHashOperations.put("DOOR:" + monitorinstrument.getChannel(), monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
                        if ("1".equals(monitorinstrument.getChannel())) {
                            //默认通道一绑定监控co2 o2 温度
                            objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
                        }
                    } else {
                        objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
                    }
                    List<InstrumentMonitorInfoModel> list = new ArrayList<InstrumentMonitorInfoModel>();
                    list = instrumentMonitorInfoMapper.selectInstrumentManyInfo(instrumentno);
                    for (InstrumentMonitorInfoModel instrumentMonitorInfoModel : list) {
                        objectObjectObjectHashOperations.put("insprobe"+hospitalcode, instrumentMonitorInfoModel.getInstrumentno() + ":" + instrumentMonitorInfoModel.getInstrumentconfigid(), JsonUtil.toJson(instrumentMonitorInfoModel));
                    }
                    return apiResponse;
                }
            }
        } catch (Exception e) {
            LOGGER.error("匹配设备探头失败，原因：" + e.getMessage());
            apiResponse.setCode(ApiResponse.FAILED);
            apiResponse.setMessage("服务异常，请联系管理员");
            return apiResponse;
        }
        //生成探头
        Monitorinstrument monitorinstrument = new Monitorinstrument();
        try {
            monitorinstrument.setInstrumenttypeid(instrumenttypeid);
            monitorinstrument.setEquipmentno(monitorequipment.getEquipmentno());
            monitorinstrument.setInstrumentname(monitorequipment.getEquipmentname() + "探头");
            monitorinstrument.setSn(equipmentInfoModel.getSn());
            monitorinstrument.setChannel(equipmentInfoModel.getChannel());
            //默认是3次
            monitorinstrument.setAlarmtime(3);
            monitorinstrument.setInstrumentno(UUID.randomUUID().toString().replaceAll("-", ""));
            monitorinstrument.setHospitalcode(hospitalcode);
            monitorinstrument = monitorInstrumentDao.save(monitorinstrument);
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            if (StringUtils.isEmpty(monitorinstrument.getChannel())) {
                objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
            } else {
                if ("2".equals(monitorinstrument.getChannel())) {
                    objectObjectObjectHashOperations.put("DOOR:2", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
                } else {
                    objectObjectObjectHashOperations.put("DOOR:1", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
                    objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
                }
            }
        } catch (Exception e) {
            LOGGER.error("生成探头失败：原因：" + e.getMessage());
            apiResponse.setMessage("生成探头失败，请联系管理员");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        //根据探头类型编号设置对应探头参数值
        List<InstrumentMonitorInfoModel>  instrumentMonitorInfoModelList = instrumentMonitorInfoMapper.selectInfoByInsTypeId(equipmentInfoModel.getInstrumenttypeid());
        if (CollectionUtils.isEmpty(instrumentMonitorInfoModelList)) {
            apiResponse.setMessage("添加设备成功，添加探头类型失败，不存在当前监控设备,请联系管理员");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        try {
            for (InstrumentMonitorInfoModel instrumentMonitorInfoModel : instrumentMonitorInfoModelList) {
                //添加探头参数
                Instrumentparamconfig instrumentparamconfig = new Instrumentparamconfig();
                instrumentparamconfig.setInstrumentparamconfigno(UUID.randomUUID().toString().replaceAll("-", ""));
                //默认是不报警
                instrumentparamconfig.setWarningphone("0");
                instrumentparamconfig.setHighlimit(instrumentMonitorInfoModel.getHighlimit());
                instrumentparamconfig.setLowlimit(instrumentMonitorInfoModel.getLowlimit());
                instrumentparamconfig.setInstrumentconfigid(instrumentMonitorInfoModel.getInstrumentconfigid());
                instrumentparamconfig.setInstrumentname(monitorinstrument.getInstrumentname());
                instrumentparamconfig.setInstrumentno(monitorinstrument.getInstrumentno());
                instrumentparamconfig.setInstrumenttypeid(instrumentMonitorInfoModel.getInstrumenttypeid());
                BigDecimal saturation = instrumentMonitorInfoModel.getSaturation();
                if (null!=saturation){
                    instrumentparamconfig.setSaturation(saturation);
                }
                instrumentparamconfig.setAlarmtime(3);
                instrumentparamconfig = instrumentParamConfigDao.save(instrumentparamconfig);
                HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
                InstrumentMonitorInfoModel instrumentMonitorInfoModel1 = instrumentMonitorInfoMapper.selectInstrumentOneInfo(instrumentparamconfig.getInstrumentparamconfigno());
                objectObjectObjectHashOperations.put("insprobe"+hospitalcode, instrumentparamconfig.getInstrumentno() + ":" + instrumentparamconfig.getInstrumentconfigid(), JsonUtil.toJson(instrumentMonitorInfoModel1));
            }
        } catch (Exception e) {
            LOGGER.error("添加探头参数失败,原因:" + e.getMessage());
            apiResponse.setCode(ApiResponse.FAILED);
            apiResponse.setMessage("探头添加失败：请自行添加探头或联系管理员");
            return apiResponse;
        }
        String equipmentno = monitorequipment.getEquipmentno();
        String usernames = equipmentInfoModel.getUsernames();
        EquipmentInfoModel model = new EquipmentInfoModel();
        //获取医院名称
        String hospitalNameByEquipmentno = monitorInstrumentMapper.getHospitalNameByEquipmentno(equipmentno);
        EquipmentInfoModel one = monitorInstrumentMapper.getEquipmentInfoByEquipmentno(equipmentno);
        updateRecordService.updateEquipmentMonitor(one.getEquipmentname(),hospitalNameByEquipmentno,usernames,model,equipmentInfoModel,"0","0");
        return apiResponse;
    }

    @Override
    public ApiResponse<String> addWiredMonitorEquipment(EquipmentInfoModel equipmentInfoModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        Monitorequipment monitorequipment = new Monitorequipment();
        String hospitalcode = equipmentInfoModel.getHospitalcode();
        String equipmenttypeid = equipmentInfoModel.getEquipmenttypeid();
        String equipmentname = equipmentInfoModel.getEquipmentname();
        String equipmentbrand = equipmentInfoModel.getEquipmentbrand();
        boolean clientvisible = equipmentInfoModel.isClientvisible();
        Integer instrumenttypeid = equipmentInfoModel.getInstrumenttypeid();
        //添加设备
        monitorequipment.setEquipmentno(UUID.randomUUID().toString().replaceAll("-", ""));
        monitorequipment.setEquipmentbrand(equipmentbrand);
        monitorequipment.setHospitalcode(hospitalcode);
        monitorequipment.setClientvisible(clientvisible);
        monitorequipment.setEquipmentname(equipmentname);
        monitorequipment.setEquipmenttypeid(equipmenttypeid);
        try {
            monitorequipment = monitorEquipmentDao.save(monitorequipment);
        } catch (Exception e) {
            LOGGER.error("添加设备失败：执行JPA--sava方法出错：" + e.getMessage());
            apiResponse.setMessage("添加设备失败：执行JPA--sava方法出错：" + e.getMessage());
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }        //添加探头
        List<WiredInstrumentModel> list = equipmentInfoModel.getList();
        for (WiredInstrumentModel wiredInstrumentModel : list) {
            Monitorinstrument monitorinstrument = new Monitorinstrument();
            monitorinstrument.setInstrumenttypeid(instrumenttypeid);
            monitorinstrument.setEquipmentno(monitorequipment.getEquipmentno());
            monitorinstrument.setInstrumentname(monitorequipment.getEquipmentname() + "探头");
            //根据sn查询当前SN是否生成探头
            Monitorinstrument monitorInstrument = monitorInstrumentMapper.getMonitorInstrument(wiredInstrumentModel.getSn());
            if (monitorInstrument !=null && StringUtils.isNotEmpty(monitorInstrument.getInstrumentno())){
                monitorinstrument = monitorInstrument;
            }else {
                monitorinstrument.setSn(wiredInstrumentModel.getSn());
                //默认是3次
                monitorinstrument.setAlarmtime(3);
                monitorinstrument.setInstrumentno(UUID.randomUUID().toString().replaceAll("-", ""));
                monitorinstrument.setHospitalcode(hospitalcode);

                try {
                    monitorinstrument = monitorInstrumentDao.save(monitorinstrument);
                    //redis缓存同步
                    HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
                    objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
                } catch (Exception e) {
                    LOGGER.error("添加有线设备探头失败：执行JPA--sava方法出错：" + e.getMessage());
                    apiResponse.setMessage("添加设备失败：执行JPA--sava方法出错：" + e.getMessage());
                    apiResponse.setCode(ApiResponse.FAILED);
                    return apiResponse;
                }
            }

            Instrumentparamconfig instrumentparamconfig = new Instrumentparamconfig();
            instrumentparamconfig.setInstrumentname(monitorinstrument.getInstrumentname());
            instrumentparamconfig.setInstrumenttypeid(monitorinstrument.getInstrumenttypeid());
            instrumentparamconfig.setInstrumentno(monitorinstrument.getInstrumentno());
            instrumentparamconfig.setHighlimit(wiredInstrumentModel.getHighlimit());
            instrumentparamconfig.setLowlimit(wiredInstrumentModel.getLowlimit());
            //默认禁用
            instrumentparamconfig.setWarningphone("0");
            instrumentparamconfig.setAlarmtime(3);
            instrumentparamconfig.setInstrumentconfigid(wiredInstrumentModel.getInstrumentconfigid());
            instrumentparamconfig.setInstrumentparamconfigno(UUID.randomUUID().toString().replaceAll("-", ""));
            instrumentparamconfig = instrumentParamConfigDao.save(instrumentparamconfig);
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            InstrumentMonitorInfoModel instrumentMonitorInfoModel1 = new InstrumentMonitorInfoModel();
            instrumentMonitorInfoModel1 = instrumentMonitorInfoMapper.selectInstrumentOneInfo(instrumentparamconfig.getInstrumentparamconfigno());
            objectObjectObjectHashOperations.put("insprobe"+hospitalcode, instrumentparamconfig.getInstrumentno() + ":" + instrumentparamconfig.getInstrumentconfigid(), JsonUtil.toJson(instrumentMonitorInfoModel1));
        }
        String equipmentno = monitorequipment.getEquipmentno();
        String usernames = equipmentInfoModel.getUsernames();
        EquipmentInfoModel model = new EquipmentInfoModel();
        //获取医院名称
        String hospitalNameByEquipmentno = monitorInstrumentMapper.getHospitalNameByEquipmentno(equipmentno);
        EquipmentInfoModel one = monitorInstrumentMapper.getEquipmentInfoByEquipmentno(equipmentno);
        updateRecordService.updateEquipmentMonitor(one.getEquipmentname(),hospitalNameByEquipmentno,usernames,model,equipmentInfoModel,"0","0");
        return apiResponse;
    }

    @Override
    public ApiResponse<String> deleteMonitroEquipment(EquipmentInfoModel equipmentInfoModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        String equipmentno = equipmentInfoModel.getEquipmentno();
        try {
         //   查询是否存在探头
            Integer J = monitorInstrumentMapper.isIntrument(equipmentno);
            if (J > 0) {
                apiResponse.setMessage("删除失败，当前设备已绑定探头");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }

            String usernames = equipmentInfoModel.getUsernames();
            EquipmentInfoModel model = new EquipmentInfoModel();
            //获取医院名称
            String hospitalNameByEquipmentno = monitorInstrumentMapper.getHospitalNameByEquipmentno(equipmentno);
            EquipmentInfoModel one = monitorInstrumentMapper.getEquipmentInfoByEquipmentno(equipmentno);
            updateRecordService.updateEquipmentMonitor(one.getEquipmentname(),hospitalNameByEquipmentno,usernames,one,model,"0","2");

            monitorEquipmentDao.deleteByHospitalcodeAndEquipmentno(equipmentno);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<String> updateMonitorEquipment(EquipmentInfoModel equipmentInfoModel) {
        Monitorequipment monitorequipment = new Monitorequipment();
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        String equipmentname = equipmentInfoModel.getEquipmentname();
        String equipmenttypeid = equipmentInfoModel.getEquipmenttypeid();
        String hospitalcode = equipmentInfoModel.getHospitalcode();
        boolean clientvisible = equipmentInfoModel.isClientvisible();
        String equipmentbrand = equipmentInfoModel.getEquipmentbrand();
        String equipmentno = equipmentInfoModel.getEquipmentno();
        try {
            monitorequipment.setEquipmentno(equipmentno);
            monitorequipment.setEquipmenttypeid(equipmenttypeid);
            monitorequipment.setEquipmentname(equipmentname);
            monitorequipment.setClientvisible(clientvisible);
            monitorequipment.setHospitalcode(hospitalcode);
            monitorequipment.setEquipmentbrand(equipmentbrand);
            String usernames = equipmentInfoModel.getUsernames();
            EquipmentInfoModel model = new EquipmentInfoModel();
            //获取医院名称
            String hospitalNameByEquipmentno = monitorInstrumentMapper.getHospitalNameByEquipmentno(equipmentno);
            EquipmentInfoModel one = monitorInstrumentMapper.getEquipmentInfoByEquipmentno(equipmentno);
            updateRecordService.updateEquipmentMonitor(one.getEquipmentname(),hospitalNameByEquipmentno,usernames,one,equipmentInfoModel,"0","1");
            monitorEquipmentDao.save(monitorequipment);
            //设备的禁用与启用需要走redis
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<Page<MonitorEquipmentInfoModel>> selectEquipmentInfoPage(String fuzzy, String hospitalcode, String equipmenttypeid, Integer pagesize, Integer pagenum) {
        ApiResponse<Page<MonitorEquipmentInfoModel>> apiResponse = new ApiResponse<Page<MonitorEquipmentInfoModel>>();
        List<MonitorEquipmentInfoModel> monitorEquipmentInfoModels = new ArrayList<MonitorEquipmentInfoModel>();
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
            pageUserModel.setEquipmenttypeid(equipmenttypeid);
            monitorEquipmentInfoModels = monitorEquipmentMapper.selectEquipmentPage(page, pageUserModel);
            if (CollectionUtils.isEmpty(monitorEquipmentInfoModels)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("无设备信息");
                return apiResponse;
            }
            PageInfo<MonitorEquipmentInfoModel> pageInfo = new PageInfo<MonitorEquipmentInfoModel>(monitorEquipmentInfoModels);
            apiResponse.setPage(pageInfo);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<List<Monitorequipment>> selectEquipmentByCode(String hospitalcode, String equipmenttypeid) {
        ApiResponse<List<Monitorequipment>> apiResponse = new ApiResponse<List<Monitorequipment>>();
        List<Monitorequipment> list = new ArrayList<Monitorequipment>();
        try {
            list = monitorEquipmentMapper.selectEquipmentByCode(hospitalcode, equipmenttypeid);
            if (CollectionUtils.isEmpty(list)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("无设备信息");
                return apiResponse;
            }
            apiResponse.setResult(list);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<List<Monitorinstrumenttype>> showInstrumentType(String hospitalcode) {
        ApiResponse<List<Monitorinstrumenttype>> apiResponse = new ApiResponse<List<Monitorinstrumenttype>>();
        List<Monitorinstrumenttype> list = new ArrayList<Monitorinstrumenttype>();
        try {
            // list = monitorInstrumentMapper.showInstrumenttpid(hospitalcode);
            list = monitorInstrumentTypeDao.findAll();
            for (int i = list.size() - 1; i >= 0; i--) {
                String instrumenttypename = list.get(i).getInstrumenttypename();
                if ("有线设备".equals(instrumenttypename)) {
                    list.remove(i);
                }

            }
            apiResponse.setResult(list);
        } catch (Exception e) {
            LOGGER.error("请求未绑定设备探头监控设备类型信息失败,原因：" + e.getMessage());
            apiResponse.setCode(ApiResponse.FAILED);
            apiResponse.setMessage("服务异常，请联系管理员");
            return apiResponse;
        }
        return apiResponse;
    }

    @Override
    public ApiResponse<List<Monitorinstrument>> showInstrumentInfo(String hospitalcode, Integer instrumenttypeid, String channel) {
        ApiResponse<List<Monitorinstrument>> apiResponse = new ApiResponse<List<Monitorinstrument>>();
        List<Monitorinstrument> list = new ArrayList<Monitorinstrument>();
        try {
            if (StringUtils.isEmpty(channel)) {
                list = monitorInstrumentMapper.showInstrument(hospitalcode, instrumenttypeid, channel);
            } else {
                list = monitorInstrumentMapper.showInstruments(hospitalcode, instrumenttypeid, channel);
            }
        } catch (Exception e) {
            LOGGER.error("请求未绑定设备探头SN信息失败,原因：" + e.getMessage());
            apiResponse.setCode(ApiResponse.FAILED);
            apiResponse.setMessage("服务异常，请联系管理员");
            return apiResponse;
        }
        if (CollectionUtils.isEmpty(list)) {
            apiResponse.setMessage("当前无SN");
        } else {
            apiResponse.setResult(list);
        }
        return apiResponse;
    }

    @Override
    public void searchEqByTwoYear(HttpServletResponse response) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.minusYears(2);
        int year = localDateTime.getYear();
        //获取年份
        String substring = String.valueOf(year).substring(2, 4);
        //获取周数
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY,1);
        int weeks = localDateTime.get(weekFields.weekOfYear());
        //获取2两年前sn规则
        String sn =substring+weeks;
        List<AllInstrumentInfoModel> allInstrumentInfoModels = instrumentMonitorInfoMapper.searchEqByTwoYear(sn);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("综合满意度评分结果");
        sheet.setDefaultColumnWidth((short)18);
        String fileName = "截至今日2年前sn号导出" + ".xls";
        String[] headers = { "医院名称", "设备类型", "设备名称", "设备型号","设备sn号" };
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 4);
        sheet.addMergedRegion(region);
        HSSFRow rowTitle = sheet.createRow(0);
        Cell oneCell = rowTitle.createCell(0);
        oneCell.setCellValue("截至今日2年前sn号");// 设置标题内容
        // 合并的单元格样式
        HSSFCellStyle boderStyle = workbook.createCellStyle();
        boderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        boderStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        boderStyle.setWrapText(true);
        oneCell.setCellStyle(boderStyle);
        HSSFRow row = sheet.createRow(1);
        // 在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        int rowNum = 2;
        // 在表中存放查询到的数据放入对应的列
        for (AllInstrumentInfoModel allInstrumentInfoModel : allInstrumentInfoModels) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(allInstrumentInfoModel.getHospitalname());
            row1.createCell(1).setCellValue(allInstrumentInfoModel.getEquipmenttypename());
            row1.createCell(2).setCellValue(allInstrumentInfoModel.getEquipmentname());
            String sn1 = allInstrumentInfoModel.getSn();
            if (StringUtils.isNotEmpty(sn1)){
                String s = MtUnConnectedSensorFilter.mtCheck(sn1.substring(4, 6));
                row1.createCell(3).setCellValue(s);
            }else {
                row1.createCell(3).setCellValue("null");
            }
            row1.createCell(4).setCellValue(sn1);
            rowNum++;
        }
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static  void  main(String args[]){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.minusYears(2);
        int year = localDateTime.getYear();
        //获取年份
        String substring = String.valueOf(year).substring(2, 4);
        //获取周数
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY,1);
        int weeks = localDateTime.get(weekFields.weekOfYear());
       System.out.println(substring+weeks);

    }
}
