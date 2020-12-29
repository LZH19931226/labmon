package com.hc.service.serviceimpl;

import com.hc.dao.WarningRecordInfoDao;
import com.hc.dao.WarningrecordSortDao;
import com.hc.entity.Monitorequipmentlastdata;
import com.hc.entity.WarningRecordInfo;
import com.hc.mapper.laboratoryFrom.EquipmentInfoMapper;
import com.hc.mapper.laboratoryFrom.WarningrecordInfoMapper;
import com.hc.model.*;
import com.hc.service.WarningRecordInfoService;
import com.hc.utils.ApiResponse;
import com.hc.utils.TimeHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author LiuZhiHao
 * @date 2020/6/8 14:46
 * 描述:
 **/
@Service
public class WarningRecordInfoServiceImpl implements WarningRecordInfoService {


    @Autowired
    private WarningRecordInfoDao wreDao;
    @Autowired
    private WarningrecordInfoMapper warningrecordInfoMapper;
    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;
    @Autowired
    private WarningrecordSortDao warningrecordSortDao;

    @Override
    public ApiResponse<WarningRecordInfo> instwarningrecordinfo(WarningRecordInfo warningrecordinfo) {
        ApiResponse<WarningRecordInfo> apiResponse = new ApiResponse<>();
            int id = warningrecordinfo.getId();
            if (id != 0) {
                warningrecordinfo.setUpdatetime(new Date());
                wreDao.updateWarningRecordInfo(warningrecordinfo.getInfo(), warningrecordinfo.getUpdateuser(), warningrecordinfo.getId());
                apiResponse.setResult(warningrecordinfo);
                return apiResponse;
            } else {
                warningrecordinfo.setCreatetime(new Date());
            }
            wreDao.save(warningrecordinfo);
        apiResponse.setResult(warningrecordinfo);
        return apiResponse;
    }

    /*
CO2	currentcarbondioxide
O2	currento2
VOC	currentvoc
TEMP	currenttemperature
RH	currenthumidity
PRESS	currentairflow
QC
PM2.5	currentpm25
PM10	currentpm10
UPS
DOOR	currentdoorstate
甲醛	currentformaldehyde
TEMP1	currenttemperature1
TEMP2	currenttemperature2
TEMP3	currenttemperature3
TEMP4	currenttemperature4
TEMP5	currenttemperature5
TEMP6	currenttemperature6
TEMP7	currenttemperature7
TEMP8	currenttemperature8
TEMP9	currenttemperature9
TEMP10	currenttemperature10
LEFTTEMP	currentlefttemperature
RIGHTTEMP	currentrigthtemperature
气流	currentairflow1
DIFFTEMP	currenttemperaturediff
PM5	currentpm5
PM0.5	currentpm05
LEFTCOVERTEMP	currentleftcovertemperature
LEFTENDTEMP	currentleftendtemperature
左气流	currentleftairflow
RIGHTCOVERTEMP	currentrightcovertemperature
RIGHTENDTEMP	currentrightendtemperature
右气流	currentrightairflow
QCL
N2	currentn2
     */
    @Override
    public ApiResponse<CurveInfoModel> getWarningCurveData(String warningRecordId, String startTime, String endTime) {
        ApiResponse<CurveInfoModel> apiResponse = new ApiResponse<>();
        String search = "monitorequipmentlastdata";
        // 判断当前日期是否为当前月份
        boolean flag = TimeHelper.isCurrentMonth(startTime);
        if (!flag) {
            // 当前月份是几月就是几月
            String year = endTime.substring(0, 4);
            String month = endTime.substring(5, 7);
            search = "monitorequipmentlastdata" + "_" + year + month;
        }
        WarningCurveDatamModel warningCurveData = warningrecordInfoMapper.getWarningCurveData(warningRecordId);
        if(warningCurveData==null){
            apiResponse.setCode(ApiResponse.NOT_FOUND);
            apiResponse.setMessage("暂无数据");
            return apiResponse;
        }
        String instrumentconfigname1 = warningCurveData.getInstrumentconfigname();
        //电量无曲线
        if (StringUtils.equalsAnyIgnoreCase(instrumentconfigname1,"QC","UPS","DOOR","voltage")){
            apiResponse.setCode(ApiResponse.NOT_FOUND);
            apiResponse.setMessage("市电,电量无曲线");
            return apiResponse;
        }
        String instrumentconfigname = warningCurveData.getInstrumentconfigname();
        MonitortlastdataTypeModel monitortlastdataTypeModel = new MonitortlastdataTypeModel();
        monitortlastdataTypeModel.setEquipmentno(warningCurveData.getEquipmentno());
        monitortlastdataTypeModel.setStartTime(startTime);
        monitortlastdataTypeModel.setEndTime(endTime);
        monitortlastdataTypeModel.setTableName(search);
        monitortlastdataTypeModel.setType(changeInstrumentConfigName(instrumentconfigname));
        List<Monitorequipmentlastdata> monitorequipmentlastdataByType = equipmentInfoMapper.getMonitorequipmentlastdataByType(monitortlastdataTypeModel);

        if (CollectionUtils.isEmpty(monitorequipmentlastdataByType)){
            apiResponse.setCode(ApiResponse.NOT_FOUND);
            apiResponse.setMessage("暂无数据");
            return apiResponse;
        }
        CurveInfoModel curveInfoModel = produceCurveInfo(monitorequipmentlastdataByType);
        apiResponse.setResult(curveInfoModel);
        return apiResponse;
    }

    @Override
    public ApiResponse<String> zfbwarningRuleSend(String warningRecordSortId) {
        warningrecordSortDao.zfbwarningRuleSend(warningRecordSortId);
        return ApiResponse.saveSuccess();
    }

    private String changeInstrumentConfigName(String instrumentconfigname) {
        switch (instrumentconfigname) {
            case "CO2":
                return "currentcarbondioxide";
            case "O2":
                return "currento2";
            case "VOC":
                return "currentvoc";
            case "TEMP":
                return "currenttemperature";
            case "RH":
                return "currenthumidity";
            case "PRESS":
                return "currentairflow";
            case "PM2.5":
                return "currentpm25";
            case "PM10":
                return "currentpm10";
            case "DOOR":
                return "currentdoorstate";
            case "甲醛":
                return "currentformaldehyde";
            case "TEMP1":
                return "currenttemperature1";
            case "TEMP2":
                return "currenttemperature2";
            case "TEMP3":
                return "currenttemperature3";
            case "TEMP4":
                return "currenttemperature4";
            case "TEMP5":
                return "currenttemperature5";
            case "TEMP6":
                return "currenttemperature6";
            case "TEMP7":
                return "currenttemperature7";
            case "TEMP8":
                return "currenttemperature8";
            case "TEMP9":
                return "currenttemperature9";
            case "TEMP10":
                return "currenttemperature10";
            case "LEFTTEMP":
                return "currentlefttemperature";
            case "RIGHTTEMP":
                return "currentrigthtemperature";
            case "气流":
                return "currentairflow1";
            case "DIFFTEMP":
                return "currenttemperaturediff";
            case "PM5":
                return "currentpm5";
            case "PM0.5":
                return "currentpm05";
            case "LEFTCOVERTEMP":
                return "currentleftcovertemperature";
            case "LEFTENDTEMP":
                return "currentleftendtemperature";
            case "左气流":
                return "currentleftairflow";
            case "RIGHTCOVERTEMP":
                return "currentrightcovertemperature";
            case "RIGHTENDTEMP":
                return "currentrightendtemperature";
            case "右气流":
                return "currentrightairflow";
            case "N2":
                return "currentn2";
            case "leftCompartmentHumidity":
                return instrumentconfigname;
            case "rightCompartmentHumidity":
                return instrumentconfigname;
            case "voltage":
                return instrumentconfigname;
            default:
                break;
        }
        return null;
     }

     public CurveInfoModel produceCurveInfo(List<Monitorequipmentlastdata> info){
         CurveInfoModel curveInfoModel = new CurveInfoModel();
         List<String> temp = new ArrayList<String>();
         List<String> tempTime = new ArrayList<String>();
         List<String> CO2 = new ArrayList<String>();
         List<String> CO2Time = new ArrayList<String>();
         List<String> O2 = new ArrayList<String>();
         List<String> O2Time = new ArrayList<String>();
         List<String> VOC = new ArrayList<String>();
         List<String> VOCTime = new ArrayList<String>();
         List<String> RH = new ArrayList<String>();
         List<String> RHTime = new ArrayList<String>();
         List<String> PM5 = new ArrayList<String>();
         List<String> PM5Time = new ArrayList<String>();
         List<String> PM05 = new ArrayList<String>();
         List<String> PM05Time = new ArrayList<String>();
         List<String> PM25 = new ArrayList<String>();
         List<String> PM25Time = new ArrayList<String>();
         List<String> PM10 = new ArrayList<String>();
         List<String> PM10Time = new ArrayList<String>();
         List<String> JQ = new ArrayList<String>();
         List<String> JQTime = new ArrayList<String>();
         List<String> PRESS = new ArrayList<String>();
         List<String> PRESSTime = new ArrayList<String>();
         List<String> airflow = new ArrayList<String>();
         List<String> airflowTime = new ArrayList<String>();
         List<String> lefttemp = new ArrayList<String>();
         List<String> lefttempTime = new ArrayList<String>();
         List<String> righttemp = new ArrayList<String>();
         List<String> righttempTime = new ArrayList<String>();
         List<String> temp1 = new ArrayList<String>();
         List<String> temp1Time = new ArrayList<String>();
         List<String> temp2 = new ArrayList<String>();
         List<String> temp2Time = new ArrayList<String>();
         List<String> temp3 = new ArrayList<String>();
         List<String> temp3Time = new ArrayList<String>();
         List<String> temp4 = new ArrayList<String>();
         List<String> temp4Time = new ArrayList<String>();
         List<String> temp5 = new ArrayList<String>();
         List<String> temp5Time = new ArrayList<String>();
         List<String> temp6 = new ArrayList<String>();
         List<String> temp6Time = new ArrayList<String>();
         List<String> temp7 = new ArrayList<String>();
         List<String> temp7Time = new ArrayList<String>();
         List<String> temp8 = new ArrayList<String>();
         List<String> temp8Time = new ArrayList<String>();
         List<String> temp9 = new ArrayList<String>();
         List<String> temp9Time = new ArrayList<String>();
         List<String> temp10 = new ArrayList<String>();
         List<String> temp10Time = new ArrayList<String>();
         List<String> tempdiff = new ArrayList<String>();
         List<String> tempdiffTime = new ArrayList<String>();
         List<String> leftcovertemp = new ArrayList<String>();
         List<String> leftcovertempTime = new ArrayList<String>();
         List<String> leftendtemp = new ArrayList<String>();
         List<String> leftendTime = new ArrayList<String>();
         List<String> leftair = new ArrayList<String>();
         List<String> leftairTime = new ArrayList<String>();
         List<String> rightcovertemp = new ArrayList<String>();
         List<String> rightcovertempTime = new ArrayList<String>();
         List<String> rightendtemp = new ArrayList<String>();
         List<String> rightendtempTime = new ArrayList<String>();
         List<String> rightair = new ArrayList<String>();
         List<String> rightairTime = new ArrayList<String>();
         List<String> n2 = new ArrayList<String>();
         List<String> n2Time = new ArrayList<String>();
         List<String> leftCompartmentHumidity = new ArrayList<String>();
         List<String> leftCompartmentHumidityTime = new ArrayList<String>();
         List<String> rightCompartmentHumidity = new ArrayList<String>();
         List<String> rightCompartmentHumidityTime = new ArrayList<String>();
         for (Monitorequipmentlastdata lastDataModel : info) {
             //       lastDataModel.setInputdatetime(TimeHelper.formats(lastDataModel.getInputdatetime()));
             //液氮罐是否存在差值
             String da = TimeHelper.formats(lastDataModel.getInputdatetime());
             //培养箱气流有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentairflow1())) {
                 airflow.add(lastDataModel.getCurrentairflow1());
                 airflowTime.add(da);
             }
             //是否存在左舱室温度
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentlefttemperature())) {
                 lefttemp.add(lastDataModel.getCurrentlefttemperature());
                 lefttempTime.add(da);
             }
             //是否存在右舱室温度
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentrigthtemperature())) {
                 righttemp.add(lastDataModel.getCurrentrigthtemperature());
                 righttempTime.add(da);
             }
             //温度1有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature1())) {
                 temp1.add(lastDataModel.getCurrenttemperature1());
                 temp1Time.add(da);
             }
             //温度2有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature2())) {
                 temp2.add(lastDataModel.getCurrenttemperature2());
                 temp2Time.add(da);
             }
             //温度3有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature3())) {
                 temp3.add(lastDataModel.getCurrenttemperature3());
                 temp3Time.add(da);
             }
             //温度4有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature4())) {
                 temp4.add(lastDataModel.getCurrenttemperature4());
                 temp4Time.add(da);
             }
             //温度5有无值
             if (!StringUtils.isEmpty(lastDataModel.getCurrenttemperature5())) {
                 temp5.add(lastDataModel.getCurrenttemperature5());
                 temp5Time.add(da);
             }
             //温度6有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature6())) {
                 temp6.add(lastDataModel.getCurrenttemperature6());
                 temp6Time.add(da);
             }
             //温度7有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature7())) {
                 temp7.add(lastDataModel.getCurrenttemperature7());
                 temp7Time.add(da);
             }
             //温度8有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature8())) {
                 temp8.add(lastDataModel.getCurrenttemperature8());
                 temp8Time.add(da);
             }
             //温度9有无值
             if (!StringUtils.isEmpty(lastDataModel.getCurrenttemperature9())) {
                 temp9.add(lastDataModel.getCurrenttemperature9());
                 temp9Time.add(da);
             }
             //温度10有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature10())) {
                 temp10.add(lastDataModel.getCurrenttemperature10());
                 temp10Time.add(da);
             }
             //温度有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature())) {
                 // if (!"A".equals() && !"B".equals(lastDataModel.getCurrenttemperature()) && !"C".equals(lastDataModel.getCurrenttemperature()) && !"D".equals(lastDataModel.getCurrenttemperature()) && !"E".equals(lastDataModel.getCurrenttemperature())) {
                 temp.add(lastDataModel.getCurrenttemperature());
                 tempTime.add(da);

             }

             //CO2有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentcarbondioxide())) {
                 //if (!"A".equals(lastDataModel.getCurrentcarbondioxide()) && !"B".equals(lastDataModel.getCurrentcarbondioxide()) && !"C".equals(lastDataModel.getCurrentcarbondioxide()) && !"D".equals(lastDataModel.getCurrentcarbondioxide()) && !"E".equals(lastDataModel.getCurrentcarbondioxide())) {
                 CO2.add(lastDataModel.getCurrentcarbondioxide());
                 CO2Time.add(da);
                 //}
             }
             //氧气有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrento2())) {
                 // if (!"A".equals(lastDataModel.getCurrento2()) && !"B".equals(lastDataModel.getCurrento2()) && !"C".equals(lastDataModel.getCurrento2()) && !"D".equals(lastDataModel.getCurrento2()) && !"E".equals(lastDataModel.getCurrento2())) {
                 O2.add(lastDataModel.getCurrento2());
                 O2Time.add(da);
                 //}
             }
             //voc 有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentvoc())) {
                 VOC.add(lastDataModel.getCurrentvoc());
                 VOCTime.add(da);
             }
             //湿度有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrenthumidity())) {
                 RH.add(lastDataModel.getCurrenthumidity());
                 RHTime.add(da);
             }
             //pm2.5有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm25())) {
                 PM25.add(lastDataModel.getCurrentpm25());
                 PM25Time.add(da);
             }
             //pm5有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm5())) {
                 PM5.add(lastDataModel.getCurrentpm5());
                 PM5Time.add(da);
             }
             //pm05有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm05())) {
                 PM05.add(lastDataModel.getCurrentpm05());
                 PM05Time.add(da);
             }
             //PM10有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm10())) {
                 PM10.add(lastDataModel.getCurrentpm10());
                 PM10Time.add(da);
             }
             //甲醛有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentformaldehyde())) {
                 JQ.add(lastDataModel.getCurrentformaldehyde());
                 JQTime.add(da);
             }
             //压力有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentairflow())) {
                 PRESS.add(lastDataModel.getCurrentairflow());
                 PRESSTime.add(da);
             }
             //左盖板温度有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftcovertemperature())) {
                 leftcovertemp.add(lastDataModel.getCurrentleftcovertemperature());
                 leftcovertempTime.add(da);
             }
             // 左底板温度有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftendtemperature())) {
                 leftendtemp.add(lastDataModel.getCurrentleftendtemperature());
                 leftendTime.add(da);
             }
             //左气流
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftairflow())) {
                 leftair.add(lastDataModel.getCurrentleftairflow());
                 leftairTime.add(da);
             }
             //右盖板温度
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightcovertemperature())) {
                 rightcovertemp.add(lastDataModel.getCurrentrightcovertemperature());
                 rightcovertempTime.add(da);
             }
             //右底板温度
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightendtemperature())) {
                 rightendtemp.add(lastDataModel.getCurrentrightendtemperature());
                 rightendtempTime.add(da);
             }
             //右气流
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightairflow()) ) {
                 rightair.add(lastDataModel.getCurrentrightairflow());
                 rightairTime.add(da);
             }
             //N2 有无值
             if (StringUtils.isNotEmpty(lastDataModel.getCurrentn2())) {
                 n2.add(lastDataModel.getCurrentn2());
                 n2Time.add(da);
             }
             if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperaturediff())) {
                 tempdiff.add(lastDataModel.getCurrenttemperaturediff());
                 tempdiffTime.add(da);
             }
             if (StringUtils.isNotEmpty(lastDataModel.getLeftCompartmentHumidity())) {
                 leftCompartmentHumidity.add(lastDataModel.getLeftCompartmentHumidity());
                 leftCompartmentHumidityTime.add(da);
             }
             if (StringUtils.isNotEmpty(lastDataModel.getRightCompartmentHumidity())) {
                 rightCompartmentHumidity.add(lastDataModel.getRightCompartmentHumidity());
                 rightCompartmentHumidityTime.add(da);
             }

         }
         if (CollectionUtils.isNotEmpty(n2)) {
             CurveDataModel curveDataModel = generateCurveDataModel(n2,n2Time);
             curveInfoModel.setN2(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(leftcovertemp)) {
             CurveDataModel curveDataModel = generateCurveDataModel(leftcovertemp,leftcovertempTime);
             curveInfoModel.setLeftcovertemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(leftendtemp)) {
             CurveDataModel curveDataModel = generateCurveDataModel(leftendtemp,leftendTime);
             curveInfoModel.setLeftendtemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(leftair)) {
             CurveDataModel curveDataModel = generateCurveDataModel(leftair,leftairTime);
             curveInfoModel.setLeftair(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(rightcovertemp)) {
             CurveDataModel curveDataModel = generateCurveDataModel(rightcovertemp,rightcovertempTime);
             curveInfoModel.setRightcovertemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(rightendtemp)) {
             CurveDataModel curveDataModel = generateCurveDataModel(rightendtemp, rightendtempTime);
             curveInfoModel.setRightendtemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(rightair)) {
             CurveDataModel curveDataModel = generateCurveDataModel(rightair, rightairTime);
             curveInfoModel.setRightair(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(tempdiff)) {
             CurveDataModel curveDataModel = generateCurveDataModel(tempdiff, tempdiffTime);
             curveInfoModel.setDifftemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(airflow)) {
             CurveDataModel curveDataModel = generateCurveDataModel(airflow, airflowTime);
             curveInfoModel.setAirflow(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(lefttemp)) {
             CurveDataModel curveDataModel = generateCurveDataModel(lefttemp, lefttempTime);
             curveInfoModel.setLefttemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(righttemp)) {
             CurveDataModel curveDataModel = generateCurveDataModel(righttemp, righttempTime);
             curveInfoModel.setRighttemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp1)) {
             CurveDataModel curveDataModel = generateCurveDataModel(temp1, temp1Time);
             curveInfoModel.setTemp1(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp2)) {
             CurveDataModel curveDataModel = generateCurveDataModel(temp2, temp2Time);
             curveInfoModel.setTemp2(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp3)) {
             CurveDataModel curveDataModel = generateCurveDataModel(temp3, temp3Time);
             curveInfoModel.setTemp3(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp4)) {
             CurveDataModel curveDataModel = generateCurveDataModel(temp4, temp4Time);
             curveInfoModel.setTemp4(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp5)) {
             CurveDataModel curveDataModel = generateCurveDataModel(temp5, temp5Time);
             curveInfoModel.setTemp5(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp6)) {
             CurveDataModel curveDataModel = generateCurveDataModel(temp6, temp6Time);
             curveInfoModel.setTemp6(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp7)) {
             CurveDataModel curveDataModel = generateCurveDataModel(temp7, temp7Time);
             curveInfoModel.setTemp7(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp8)) {
             CurveDataModel curveDataModel = generateCurveDataModel(temp8, temp8Time);
             curveInfoModel.setTemp8(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp9)) {
             CurveDataModel curveDataModel = generateCurveDataModel(temp9, temp9Time);
             curveInfoModel.setTemp9(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp10)) {
             CurveDataModel curveDataModel = generateCurveDataModel(temp10, temp10Time);
             curveInfoModel.setTemp10(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp)) {
             CurveDataModel curveDataModel = generateCurveDataModel(temp, tempTime);
             curveInfoModel.setTemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(CO2)) {
             CurveDataModel curveDataModel = generateCurveDataModel(CO2, CO2Time);
             curveInfoModel.setCo2(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(O2)) {
             CurveDataModel curveDataModel = generateCurveDataModel(O2, O2Time);
             curveInfoModel.setO2(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(VOC)) {
             CurveDataModel curveDataModel = generateCurveDataModel(VOC, VOCTime);
             curveInfoModel.setVoc(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(RH)) {
             CurveDataModel curveDataModel = generateCurveDataModel(RH, RHTime);
             curveInfoModel.setRh(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(PM25)) {
             CurveDataModel curveDataModel = generateCurveDataModel(PM25, PM25Time);
             curveInfoModel.setPm25(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(PM5)) {
             CurveDataModel curveDataModel = generateCurveDataModel(PM5, PM5Time);
             curveInfoModel.setPm5(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(PM05)) {
             CurveDataModel curveDataModel = generateCurveDataModel(PM05, PM05Time);
             curveInfoModel.setPm05(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(PM10)) {
             CurveDataModel curveDataModel = generateCurveDataModel(PM10, PM10Time);
             curveInfoModel.setPm10(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(JQ)) {
             CurveDataModel curveDataModel = generateCurveDataModel(JQ, JQTime);
             curveInfoModel.setJq(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(PRESS)) {
             CurveDataModel curveDataModel = generateCurveDataModel(PRESS, PRESSTime);
             curveInfoModel.setPress(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(leftCompartmentHumidity)) {
             CurveDataModel curveDataModel = generateCurveDataModel(leftCompartmentHumidity, leftCompartmentHumidityTime);
             curveInfoModel.setLeftCompartmentHumidity(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(rightCompartmentHumidity)) {
             CurveDataModel curveDataModel = generateCurveDataModel(rightCompartmentHumidity, rightCompartmentHumidityTime);
             curveInfoModel.setRightCompartmentHumidity(curveDataModel);
         }
         return curveInfoModel;
     }



    private CurveDataModel generateCurveDataModel(List<String> listdata, List<String> listtime){
        CurveDataModel curveDataModel = new CurveDataModel();
        curveDataModel.setXaxis(listtime);
        SeriesDataModel seriesDataModel = new SeriesDataModel();
        seriesDataModel.setDate(listdata);
        List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
        seriesDataModelList.add(seriesDataModel);
        curveDataModel.setSeries(seriesDataModelList);
        return  curveDataModel;
    }
}
