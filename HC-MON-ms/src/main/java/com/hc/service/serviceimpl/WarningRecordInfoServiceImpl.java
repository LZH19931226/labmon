package com.hc.service.serviceimpl;

import com.hc.dao.WarningRecordInfoDao;
import com.hc.entity.Monitorequipmentlastdata;
import com.hc.entity.WarningRecordInfo;
import com.hc.mapper.laboratoryFrom.EquipmentInfoMapper;
import com.hc.mapper.laboratoryFrom.WarningrecordInfoMapper;
import com.hc.model.*;
import com.hc.model.SingleTimeExcle.SingleTimeEquipmentModel;
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
        String search = "monitorequipmentlastdata";
        // 判断当前日期是否为当前月份
        boolean flag = TimeHelper.isCurrentMonth(startTime);
        if (!flag) {
            // 当前月份是几月就是几月
            String year = startTime.substring(0, 4);
            String month = startTime.substring(5, 7);
            search = "monitorequipmentlastdata" + "_" + year + month;
        }
        WarningCurveDatamModel warningCurveData = warningrecordInfoMapper.getWarningCurveData(warningRecordId);
        String instrumentconfigname = warningCurveData.getInstrumentconfigname();
        MonitortlastdataTypeModel monitortlastdataTypeModel = new MonitortlastdataTypeModel();
        monitortlastdataTypeModel.setEquipmentno(warningCurveData.getEquipmentno());
        monitortlastdataTypeModel.setStartTime(startTime);
        monitortlastdataTypeModel.setEndTime(endTime);
        monitortlastdataTypeModel.setTableName(search);
        monitortlastdataTypeModel.setType(changeInstrumentConfigName(instrumentconfigname));
        List<Monitorequipmentlastdata> monitorequipmentlastdataByType = equipmentInfoMapper.getMonitorequipmentlastdataByType(monitortlastdataTypeModel);
        ApiResponse<CurveInfoModel> apiResponse = new ApiResponse<>();
        if (CollectionUtils.isEmpty(monitorequipmentlastdataByType)){
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        CurveInfoModel curveInfoModel = produceCurveInfo(monitorequipmentlastdataByType);
        apiResponse.setResult(curveInfoModel);
        return apiResponse;
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
         }
         if (CollectionUtils.isNotEmpty(n2)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(n2Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(n2);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setN2(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(leftcovertemp)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(leftcovertempTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(leftcovertemp);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setLeftcovertemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(leftendtemp)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(leftendTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(leftendtemp);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setLeftendtemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(leftair)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(leftairTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(leftair);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setLeftair(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(rightcovertemp)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(rightcovertempTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(rightcovertemp);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setRightcovertemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(rightendtemp)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(rightendtempTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(rightendtemp);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setRightendtemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(rightair)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(rightairTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(rightair);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setRightair(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(tempdiff)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(tempdiffTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(tempdiff);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setDifftemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(airflow)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(airflowTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(airflow);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setAirflow(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(lefttemp)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(lefttempTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(lefttemp);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setLefttemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(righttemp)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(righttempTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(righttemp);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setRighttemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp1)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(temp1Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(temp1);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setTemp1(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp2)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(temp2Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(temp2);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setTemp2(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp3)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(temp3Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(temp3);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setTemp3(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp4)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(temp4Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(temp4);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setTemp4(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp5)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(temp5Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(temp5);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setTemp5(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp6)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(temp6Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(temp6);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setTemp6(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp7)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(temp7Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(temp7);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setTemp7(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp8)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(temp8Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(temp8);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setTemp8(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp9)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(temp9Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(temp9);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setTemp9(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp10)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(temp10Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(temp10);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setTemp10(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(temp)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(tempTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(temp);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setTemp(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(CO2)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(CO2Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(CO2);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setCo2(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(O2)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(O2Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(O2);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setO2(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(VOC)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(VOCTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(VOC);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setVoc(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(RH)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(RHTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(RH);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setRh(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(PM25)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(PM25Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(PM25);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setPm25(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(PM5)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(PM5Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(PM5);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setPm5(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(PM05)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(PM05Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(PM05);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setPm05(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(PM10)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(PM10Time);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(PM10);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setPm10(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(JQ)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(JQTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(JQ);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setJq(curveDataModel);
         }
         if (CollectionUtils.isNotEmpty(PRESS)) {
             CurveDataModel curveDataModel = new CurveDataModel();
             curveDataModel.setXaxis(PRESSTime);
             SeriesDataModel seriesDataModel = new SeriesDataModel();
             seriesDataModel.setDate(PRESS);
             List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
             seriesDataModelList.add(seriesDataModel);
             curveDataModel.setSeries(seriesDataModelList);
             curveInfoModel.setPress(curveDataModel);
         }
         return curveInfoModel;
     }
}
