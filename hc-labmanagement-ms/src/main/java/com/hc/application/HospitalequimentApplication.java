package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.dto.HospitalequimentDTO;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.labmanagent.OperationlogApi;
import com.hc.service.HospitalequimentService;
import com.hc.service.MonitorequipmentwarningtimeService;
import com.hc.vo.equimenttype.HospitalequimentVo;
import com.hc.vo.equimenttype.MonitorequipmentwarningtimeVo;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Component
public class HospitalequimentApplication {

    @Autowired
    private HospitalequimentService hospitalequimentService;
    @Autowired
    private MonitorequipmentwarningtimeService monitorequipmentwarningtimeService;

    @Autowired
    private OperationlogApi operationlogApi;
    /**
     * 新增医院设备类型
     * @param hospitalEquimentTypeCommand
     */
    public void addHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        hospitalequimentService.addHospitalEquimentType(hospitalEquimentTypeCommand);

    }



    public void updateHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        hospitalequimentService.updateHospitalEquimentType(hospitalEquimentTypeCommand);
    }

    public Page<HospitalequimentVo> selectHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        Page<HospitalequimentVo> page = new Page<>(hospitalEquimentTypeCommand.getPageCurrent(),hospitalEquimentTypeCommand.getPageSize());
        List<HospitalequimentVo> hospitalEquipmentVos = new ArrayList<>();
        List<HospitalequimentDTO> hospitalEquipmentList = hospitalequimentService.selectHospitalEquimentType(page,hospitalEquimentTypeCommand);
        if (CollectionUtils.isNotEmpty(hospitalEquipmentList)) {
            List<String> hospitalCodes = hospitalEquipmentList.stream().map(HospitalequimentDTO::getHospitalcode).collect(Collectors.toList());
            List<MonitorequipmentwarningtimeDTO> warningTimes  = monitorequipmentwarningtimeService.selectWarningtimeByHosCode(hospitalCodes);
            Map<String, List<MonitorequipmentwarningtimeDTO>> timesMap = new  HashedMap();
            if (CollectionUtils.isNotEmpty(warningTimes)){
                 timesMap = warningTimes.stream().collect(Collectors.groupingBy(MonitorequipmentwarningtimeDTO::getHospitalcode));
            }
            Map<String, List<MonitorequipmentwarningtimeDTO>> finalTimesMap = timesMap;
            hospitalEquipmentList.forEach(s->{
                String hospitalcode = s.getHospitalcode();
                List<MonitorequipmentwarningtimeVo> workTimeBlock = new ArrayList<>();
                if (!finalTimesMap.isEmpty()){
                    List<MonitorequipmentwarningtimeDTO> monitorequipmentwarningtimeDTOS    = finalTimesMap.get(hospitalcode);
                    if (CollectionUtils.isNotEmpty(monitorequipmentwarningtimeDTOS)){
                        workTimeBlock = new ArrayList<>();
                        List<MonitorequipmentwarningtimeVo> finalWorkTimeBlock = workTimeBlock;
                        monitorequipmentwarningtimeDTOS.forEach(f->{
                            MonitorequipmentwarningtimeVo time = MonitorequipmentwarningtimeVo.builder()
                                    .timeblockid(f.getTimeblockid())
                                    .begintime(f.getBegintime())
                                    .endtime(f.getEndtime())
                                    .build();
                            finalWorkTimeBlock.add(time);
                        });
                    }
                }
                HospitalequimentVo hosEqVo = HospitalequimentVo.builder()
                        .hospitalcode(hospitalcode)
                        .hospitalname(s.getHospitalname())
                        .equipmenttypeid(s.getEquipmenttypeid())
                        .equipmenttypename(s.getEquipmenttypename())
                        .isvisible(s.getIsvisible())
                        .alwayalarm(s.getAlwayalarm())
                        .timeout(s.getTimeout())
                        .timeouttime(s.getTimeouttime())
                        .workTimeBlock(workTimeBlock)
                        .build();
                hospitalEquipmentVos.add(hosEqVo);
            });
        }
        page.setRecords(hospitalEquipmentVos);
        return page;
    }

    public void deleteHospitalEquimentType(String hospitalCode, String equipmenttypeid) {
        hospitalequimentService.deleteHospitalEquimentType(hospitalCode,equipmenttypeid);
    }
}
