package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.dto.InstrumentconfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.my.common.core.constant.enums.MonitorinstrumentEnumCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.service.InstrumentparamconfigService;
import com.hc.service.MonitorinstrumentService;
import com.hc.vo.equimenttype.InstrumentparamconfigVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Component
public class InstrumentparamconfigApplication {

    @Autowired
    private InstrumentparamconfigService instrumentparamconfigService;

    @Autowired
    private MonitorinstrumentService monitorinstrumentService;

    /**
     * 通过设备no获取探头参数信息
     *
     * @param equipmentNo 设备id
     * @return
     */
    public List<InstrumentparamconfigVo> selectInstrumentParamConfigByEqNo(String equipmentNo) {
        List<InstrumentconfigDTO> instrumentConfigList = instrumentparamconfigService.selectInstrumentparamconfigByEqNo(equipmentNo);
        if (CollectionUtils.isNotEmpty(instrumentConfigList)) {
            List<InstrumentparamconfigVo> instrumentParamConfigVos = new ArrayList<>();
            instrumentConfigList.forEach(s -> {
                instrumentParamConfigVos.add(
                        InstrumentparamconfigVo.builder()
                                .instrumentconfigid(s.getInstrumentconfigid())
                                .instrumentconfigname(s.getInstrumentconfigname())
                                .build()
                );
            });
            return instrumentParamConfigVos;
        }
        return null;
    }

    /**
     * 新增探头信息
     *
     * @param instrumentParamConfigCommand 探头信息参数
     */
    public void insertInstrumentParamConfig(InstrumentparamconfigCommand instrumentParamConfigCommand) {

        Integer i =  instrumentparamconfigService.selectCount(instrumentParamConfigCommand.getInstrumentNo(),
                instrumentParamConfigCommand.getInstrumentconfigid(),instrumentParamConfigCommand.getInstrumenttypeid());
        if(i>0){
            throw new IedsException(MonitorinstrumentEnumCode.PROBE_INFORMATION_ALREADY_EXISTS.getMessage());
        }
        int compareTo = instrumentParamConfigCommand.getLowlimit().compareTo(instrumentParamConfigCommand.getHighlimit());
        if(compareTo>0){
            throw new IedsException(MonitorinstrumentEnumCode.THE_LOWER_LIMIT_CANNOT_EXCEED_THE_UPPER_LIMIT.getMessage());
        }
        InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO()
                .setInstrumentparamconfigno(UUID.randomUUID().toString().replaceAll("-", ""))
                .setInstrumentno(instrumentParamConfigCommand.getInstrumentNo())
                .setInstrumentconfigid(instrumentParamConfigCommand.getInstrumentconfigid())
                .setInstrumentname(instrumentParamConfigCommand.getInstrumentname())
                .setLowlimit(instrumentParamConfigCommand.getLowlimit())
                .setHighlimit(instrumentParamConfigCommand.getHighlimit())
                .setInstrumenttypeid(instrumentParamConfigCommand.getInstrumenttypeid())
                .setWarningphone(instrumentParamConfigCommand.getWarningphone())
                .setChannel(instrumentParamConfigCommand.getChannel())
                .setAlarmtime(instrumentParamConfigCommand.getAlarmtime())
                .setFirsttime(new Date())
                .setSaturation(instrumentParamConfigCommand.getSaturation());

        instrumentparamconfigService.insertInstrumentmonitor(instrumentparamconfigDTO);
    }

    /**
     * 修改探头信息
     *
     * @param instrumentParamConfigCommand 探头信息参数
     */
    public void editInstrumentParamConfig(InstrumentparamconfigCommand instrumentParamConfigCommand) {
        //计较上限值和下限值
        int compareTo = instrumentParamConfigCommand.getLowlimit().compareTo(instrumentParamConfigCommand.getHighlimit());
        if(compareTo>=0){
            throw new IedsException(MonitorinstrumentEnumCode.THE_LOWER_LIMIT_CANNOT_EXCEED_THE_UPPER_LIMIT.getMessage());
        }
        InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO()
                .setInstrumentparamconfigno(instrumentParamConfigCommand.getInstrumentparamconfigno())
                .setInstrumentno(instrumentParamConfigCommand.getInstrumentNo())
                .setInstrumentconfigid(instrumentParamConfigCommand.getInstrumentconfigid())
                .setInstrumentname(instrumentParamConfigCommand.getInstrumentname())
                .setLowlimit(instrumentParamConfigCommand.getLowlimit())
                .setHighlimit(instrumentParamConfigCommand.getHighlimit())
                .setInstrumenttypeid(instrumentParamConfigCommand.getInstrumenttypeid())
                .setWarningphone(instrumentParamConfigCommand.getWarningphone())
                .setChannel(instrumentParamConfigCommand.getChannel())
                .setSaturation(instrumentParamConfigCommand.getSaturation());
        instrumentparamconfigService.updateInfo(instrumentparamconfigDTO);
    }

    /**
     * 清除探头信息
     *
     * @param instrumentParamConfigNo 探头信息参数
     */
    public void removeInstrumentParamConfig(String instrumentParamConfigNo) {
        instrumentparamconfigService.deleteInfo(new InstrumentparamconfigDTO().setInstrumentparamconfigno(instrumentParamConfigNo));
    }


    /**
     * 分页获取探头信息
     *
     * @param instrumentParamConfigCommand 探头信息参数
     * @return
     */
    public Page<InstrumentparamconfigVo> findInstrumentParamConfig(InstrumentparamconfigCommand instrumentParamConfigCommand) {
        Page<InstrumentparamconfigVo> page = new Page<>(instrumentParamConfigCommand.getPageCurrent(), instrumentParamConfigCommand.getPageSize());
        List<InstrumentparamconfigDTO> instrumentParamConfigList = instrumentparamconfigService.findInstrumentparamconfig(page, instrumentParamConfigCommand);
        List<InstrumentparamconfigVo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(instrumentParamConfigList)) {

            for (InstrumentparamconfigDTO configDTO : instrumentParamConfigList) {
                InstrumentparamconfigVo build = InstrumentparamconfigVo
                        .builder()
                        .instrumentparamconfigno(configDTO.getInstrumentparamconfigno())
                        .hospitalname(configDTO.getHospitalname())
                        .hospitalCode(configDTO.getHospitalcode())
                        .equipmentName(configDTO.getEquipmentname())
                        .equipmenttypename(configDTO.getEquipmenttypename())
                        .instrumentname(configDTO.getInstrumentname())
                        .instrumentno(configDTO.getInstrumentno())
                        .sn(configDTO.getSn())
                        .firsttime(configDTO.getFirsttime())
                        .channel(configDTO.getChannel())
                        .instrumentconfigid(configDTO.getInstrumentconfigid())
                        .instrumentconfigname(configDTO.getInstrumentconfigname())
                        .instrumenttypeid(configDTO.getInstrumenttypeid())
                        .instrumenttypename(configDTO.getInstrumenttypename())
                        .alarmtime(configDTO.getAlarmtime())
                        .lowlimit(configDTO.getLowlimit())
                        .highlimit(configDTO.getHighlimit())
                        .saturation(configDTO.getSaturation())
                        .warningphone(configDTO.getWarningphone())
                        .calibration(configDTO.getCalibration() == null?"":configDTO.getCalibration())
                        .build();
                list.add(build);
            }
        }
        page.setRecords(list);
        return page;
    }
}
