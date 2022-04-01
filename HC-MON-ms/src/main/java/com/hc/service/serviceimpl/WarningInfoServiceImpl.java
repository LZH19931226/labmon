package com.hc.service.serviceimpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.hc.dao.WarningRecordDao;
import com.hc.entity.Warningrecord;
import com.hc.mapper.laboratoryFrom.WarningrecordInfoMapper;
import com.hc.model.*;
import com.hc.service.WarningInfoService;
import com.hc.utils.ApiResponse;
import com.hc.utils.TimeHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by 16956 on 2018-08-01.
 */
@Service
public class WarningInfoServiceImpl implements WarningInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    private WarningrecordInfoMapper warningrecordInfoMapper;
    @Autowired
    private WarningRecordDao warningRecordDao;

    @Override
    public ApiResponse<List<Warningrecord>> getWarningRecord(String hospitalcode) {
        ApiResponse<List<Warningrecord>> apiResponse = new ApiResponse<List<Warningrecord>>();
        List<Warningrecord> warningrecordList;
        try{
            warningrecordList = warningrecordInfoMapper.getWarningRecord(hospitalcode);
            if (StringUtils.isEmpty(warningrecordList)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("当前医院无报警记录");
                return apiResponse;
            }
            apiResponse.setMessage("查询成功");
            apiResponse.setCode(ApiResponse.SUCCESS);
            apiResponse.setResult(warningrecordList);
            return apiResponse;
        }catch (Exception e){
            LOGGER.error("失败：" +e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }

    }

    @Override
    public ApiResponse<Page<NewWarningRecord>> getNewWarnRecord(String hospitalcode,Integer pagesize,Integer pagenum) {
        ApiResponse<Page<NewWarningRecord>> apiResponse = new ApiResponse<Page<NewWarningRecord>>();
        List<NewWarningRecord> newWarningRecordList;
        try{
            //pagehelp 分页
            Integer start = (pagenum-1) * pagesize;
            Integer end = pagesize;
            PageRowBounds page = new PageRowBounds(start,end);
            PageUserModel pageUserModel = new PageUserModel();
            pageUserModel.setHospitalcode(hospitalcode);
            newWarningRecordList = warningrecordInfoMapper.getNewWarnRecord(page,pageUserModel);
            if (CollectionUtils.isEmpty(newWarningRecordList)) {
                apiResponse.setMessage("无最新报警信息");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            List<String> collect = newWarningRecordList.stream().map(NewWarningRecord::getInstrumentparamconfigNO).collect(Collectors.toList());
            //获取当前时间到1个月之前这中间的报警数据统和已备注数据统计
            String currentDateTime = TimeHelper.getCurrentDateTime();
            String currentDateTimeBeforOneMonth = TimeHelper.getCurrentDateTimeBeforOneMonth();
            MonitortlastdataTypeModel monitortlastdataTypeModel = new MonitortlastdataTypeModel();
            monitortlastdataTypeModel.setStartTime(currentDateTimeBeforOneMonth);
            monitortlastdataTypeModel.setEndTime(currentDateTime);
            monitortlastdataTypeModel.setInstruMentParamConfigNos(collect);
            List<NewWarningRecord> warNingRecordMonthCount = warningrecordInfoMapper.getWarNingRecordMonthCount(monitortlastdataTypeModel);
            if (CollectionUtils.isNotEmpty(warNingRecordMonthCount)){
                Map<String, List<NewWarningRecord>> collect1 = warNingRecordMonthCount.stream().collect(Collectors.groupingBy(NewWarningRecord::getInstrumentparamconfigNO));
                newWarningRecordList.forEach(s->{
                    String instrumentparamconfigNO = s.getInstrumentparamconfigNO();
                    List<NewWarningRecord> newWarningRecords = collect1.get(instrumentparamconfigNO);
                    if (CollectionUtils.isNotEmpty(newWarningRecords)){
                        NewWarningRecord newWarningRecord = newWarningRecords.get(0);
                        s.setWarningTotal(newWarningRecord.getCount1());
                    }
                });
            }
            List<NewWarningRecord> warNingRecordInfoMonthCount = warningrecordInfoMapper.getWarNingRecordInfoMonthCount(monitortlastdataTypeModel);
            if (CollectionUtils.isNotEmpty(warNingRecordInfoMonthCount)){
                Map<String, List<NewWarningRecord>> collect1 = warNingRecordInfoMonthCount.stream().collect(Collectors.groupingBy(NewWarningRecord::getInstrumentparamconfigNO));
                newWarningRecordList.forEach(s->{
                    String instrumentparamconfigNO = s.getInstrumentparamconfigNO();
                    List<NewWarningRecord> newWarningRecords = collect1.get(instrumentparamconfigNO);
                    if (CollectionUtils.isNotEmpty(newWarningRecords)){
                        NewWarningRecord newWarningRecord = newWarningRecords.get(0);
                        s.setWarningInfoTotal(newWarningRecord.getCount1());
                    }
                });
            }
            PageInfo<NewWarningRecord> pageInfo = new PageInfo<NewWarningRecord>(newWarningRecordList);
            apiResponse.setPage(pageInfo);
            return apiResponse;

        }catch(Exception e){
            LOGGER.error("失败：" +e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<Page<Warningrecord>> getInstrumentTypeHistoryWarn(String instrumentparamconfigNO,Integer pagesize,Integer pagenum,String isphone) {
            ApiResponse<Page<Warningrecord>> apiResponse = new ApiResponse<Page<Warningrecord>>();
            List<Warningrecord> warningrecordList = null;
            try{
                Integer start = (pagenum-1) * pagesize;
                Integer end = pagesize;
                PageRowBounds page = new PageRowBounds(start,end);
                if ("0".equals(isphone)){
                    warningrecordList = warningrecordInfoMapper.getInstrumentTypeHistoryWarnAll(instrumentparamconfigNO,page);
                }else {
                    warningrecordList = warningrecordInfoMapper.getInstrumentTypeHistoryWarn(instrumentparamconfigNO,page);
                }
                if (CollectionUtils.isEmpty(warningrecordList)) {
                    apiResponse.setMessage("无历史报警信息");
                    apiResponse.setCode(ApiResponse.FAILED);
                    return apiResponse;
                }
                // 将所有信息改为已读
                warningRecordDao.updateMsgFlag(instrumentparamconfigNO);
                PageInfo<Warningrecord> pageInfo = new PageInfo<Warningrecord>(warningrecordList);
                apiResponse.setPage(pageInfo);
                return apiResponse;
            }catch (Exception e) {
                LOGGER.error("失败：" +e.getMessage());
                apiResponse.setMessage("服务异常");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }

    }

    @Override
    public ApiResponse<String> isRead(String instrumentparamconfigNO) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        try{
            String pushstate = "1";
            Integer i = warningRecordDao.updatePushState(pushstate,instrumentparamconfigNO);
            if ( i > 0 ) {
                apiResponse.setMessage("修改状态成功");
            }else{
                apiResponse.setMessage("无当前探头类型信息，修改状态失败");
                apiResponse.setCode(ApiResponse.FAILED);
            }
            return apiResponse;

        }catch(Exception e) {
            LOGGER.error("失败：" +e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<String> deleteWarningInfo(PushSetModel pushSetModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        String instrumentparamconfigNO = pushSetModel.getInstrumentparamconfigNO();
        try{
            warningRecordDao.deleteByInstrumentparamconfigNO(instrumentparamconfigNO);
            return apiResponse;
        }catch(Exception e){
            LOGGER.error("删除报警信息失败，原因："+e.getMessage());
            apiResponse.setCode(ApiResponse.FAILED);
            apiResponse.setMessage("删除失败，请联系管理员");
            return apiResponse;
        }

    }

    @Override
    public ApiResponse<List<ShowData>> showData() {
        ApiResponse<List<ShowData>> apiResponse = new ApiResponse<List<ShowData>>();
        try{
            List<ShowData> showData = warningrecordInfoMapper.showData();
            if (showData.isEmpty()) {
                apiResponse.setCode(ApiResponse.NOT_FOUND);
                apiResponse.setMessage("当前无报警信息");
                return apiResponse;
            }
            // 时间排序
            Collections.sort(showData, new Comparator<ShowData>() {
                @Override
                public int compare(ShowData o1, ShowData o2) {
                    return o1.getInputdatetime().compareTo(o2.getInputdatetime());
                }
            });
            apiResponse.setResult(showData);
            return apiResponse;
        }catch (Exception e){
            LOGGER.error("获取报警信息失败，原因："+e.getMessage());
            apiResponse.setCode(ApiResponse.FAILED);
            apiResponse.setMessage("获取报警信息失败，原因："+e);
            return apiResponse;
        }
    }


}
