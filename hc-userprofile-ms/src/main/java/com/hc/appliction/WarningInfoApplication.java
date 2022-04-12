package com.hc.appliction;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.WarningrecordDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.WarningrecordService;
import com.hc.vo.waring.WarningrecordVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WarningInfoApplication {

    @Autowired
    private WarningrecordService warningrecordService;

    /**
     * 获取警告记录
     * @param hospitalcode 异常编码
     * @return
     */
    public List<WarningrecordVo> getWarningRecord(String hospitalcode) {
        List<WarningrecordDto> warningrecordDtos =  warningrecordService.getWarningRecord(hospitalcode);
        List<WarningrecordVo>  list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(warningrecordDtos)){
            warningrecordDtos.forEach(s->{
                list.add(WarningrecordVo.builder()
                        .pkid(s.getPkid())
                        .build());
            });
            return list;
        }
        return null;
    }

    /**
     * 获得新的警告记录
     * @param hospitalcode 医院编码
     * @param pagesize 页面大小
     * @param pagenum   页面数量
     * @return
     */
    public Page<WarningrecordVo> getNewWarnRecord(String hospitalcode, Integer pagesize, Integer pagenum) {
        ArrayList<WarningrecordVo> warningrecordVos = new ArrayList<>();
        Page page = new Page<>();
        page.setCurrent(pagesize);
        page.setSize(pagenum);
        List<WarningrecordDto> newWarningRecordList =  warningrecordService.getNewWarnRecord(page,hospitalcode);
        if(CollectionUtils.isEmpty(newWarningRecordList)){
            return null;
        }
        //获取InstrumentparamconfigNO集合
        List<String> collect = newWarningRecordList.stream().map(WarningrecordDto::getInstrumentparamconfigNO).collect(Collectors.toList());
        List<WarningrecordDto>  warNingRecordMonthCount= warningrecordService.getWarNingRecordMonthCount(collect);
        if(CollectionUtils.isNotEmpty(warNingRecordMonthCount)){
            Map<String, List<WarningrecordDto>> collect1 = warNingRecordMonthCount.stream().collect(Collectors.groupingBy(WarningrecordDto::getInstrumentparamconfigNO));
            newWarningRecordList.forEach(s->{
                String instrumentparamconfigNO = s.getInstrumentparamconfigNO();
                List<WarningrecordDto> newWarningRecords = collect1.get(instrumentparamconfigNO);
                if (CollectionUtils.isNotEmpty(newWarningRecords)){
                    WarningrecordDto newWarningRecord = newWarningRecords.get(0);
                    s.setWarningTotal(newWarningRecord.getCount1());
                }
            });
        }
        List<WarningrecordDto> warNingRecordInfoMonthCount = warningrecordService.getWarNingRecordInfoMonthCount(collect);
        if (CollectionUtils.isNotEmpty(warNingRecordInfoMonthCount)){
            Map<String, List<WarningrecordDto>> collect1 = warNingRecordInfoMonthCount.stream().collect(Collectors.groupingBy(WarningrecordDto::getInstrumentparamconfigNO));
            newWarningRecordList.forEach(s->{
                String instrumentparamconfigNO = s.getInstrumentparamconfigNO();
                List<WarningrecordDto> newWarningRecords = collect1.get(instrumentparamconfigNO);
                if (CollectionUtils.isNotEmpty(newWarningRecords)){
                    WarningrecordDto newWarningRecord = newWarningRecords.get(0);
                    s.setWarningInfoTotal(newWarningRecord.getCount1());
                }
            });
        }
        List<WarningrecordVo> convert = BeanConverter.convert(newWarningRecordList, WarningrecordVo.class);
        return page.setRecords(convert);
    }

    /**
     * 获取仪器类型历史警告
     * @param instrumentparamconfigNO 仪器参数配置
     * @param pagesize 页面大小
     * @param pagenum 页面数量
     * @param isphone  手机
     * @return
     */
    public Page<WarningrecordVo> getInstrumentTypeHistoryWarn(String instrumentparamconfigNO, Integer pagesize, Integer pagenum, String isphone) {
        List<WarningrecordDto> warningrecordList = null;
        Page page = new Page<>();
        page.setSize(pagesize);
        page.setCurrent(pagenum);
        if ("0".equals(isphone)){
            warningrecordList = warningrecordService.getInstrumentTypeHistoryWarnAll(instrumentparamconfigNO,page);
        }else {
            warningrecordList = warningrecordService.getInstrumentTypeHistoryWarn(instrumentparamconfigNO,page);
        }
        if(CollectionUtils.isEmpty(warningrecordList)){
            return null;
        }
        // 将所有信息改为已读
        warningrecordService.updateMsgFlag(instrumentparamconfigNO);
        return page.setRecords(warningrecordList);
    }

    @Transactional(rollbackFor = Exception.class)
    public String isRead(String instrumentparamconfigNO) {
        int i = warningrecordService.updatePushState(instrumentparamconfigNO,"1");
        return null;
    }

    public String deleteWarningInfo(WarningrecordVo warningrecordVo) {
        return null;
    }
}
