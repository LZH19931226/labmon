package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.dto.InstrumentconfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.dto.MonitorinstrumentDTO;
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


    public List<InstrumentparamconfigVo> selectInstrumentparamconfigByEqNo(String equipmentNo) {
        List<InstrumentconfigDTO> instrumentconfigDTOS = instrumentparamconfigService.selectInstrumentparamconfigByEqNo(equipmentNo);
        if (CollectionUtils.isNotEmpty(instrumentconfigDTOS)) {
            List<InstrumentparamconfigVo> instrumentparamconfigVos = new ArrayList<>();
            instrumentconfigDTOS.forEach(s -> {
                instrumentparamconfigVos.add(
                        InstrumentparamconfigVo.builder()
                                .instrumentconfigid(s.getInstrumentconfigid())
                                .instrumentconfigname(s.getInstrumentconfigname())
                                .build()
                );
            });
            return instrumentparamconfigVos;
        }
        return null;
    }

    /**
     * 新增探头信息
     * @param instrumentparamconfigCommand
     */
    public void insertInstrumentparamconfig(InstrumentparamconfigCommand instrumentparamconfigCommand) {
        String sn = instrumentparamconfigCommand.getSn();
        String hospitalCode = instrumentparamconfigCommand.getHospitalCode();
        Integer integer = monitorinstrumentService.selectCount(new MonitorinstrumentDTO().setSn(sn).setHospitalcode(hospitalCode));
        if(integer>0){
            throw new IedsException(MonitorinstrumentEnumCode.FAILED_TO_ADD_PROBE.getMessage());
        }
        InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO()
                .setInstrumentparamconfigno(UUID.randomUUID().toString().replaceAll("-",""))
                .setInstrumentno(instrumentparamconfigCommand.getInstrumentNo())
                .setInstrumentconfigid(instrumentparamconfigCommand.getInstrumentconfigid())
                .setInstrumentname(instrumentparamconfigCommand.getInstrumentname())
                .setLowlimit(instrumentparamconfigCommand.getLowlimit())
                .setHighlimit(instrumentparamconfigCommand.getHighlimit())
                .setInstrumenttypeid(instrumentparamconfigCommand.getInstrumenttypeid())
                .setWarningphone(instrumentparamconfigCommand.getWarningphone())
                .setChannel(instrumentparamconfigCommand.getChannel())
                .setFirsttime(new Date())
                .setSaturation(instrumentparamconfigCommand.getSaturation());
        instrumentparamconfigService.insertInstrumentmonitor(instrumentparamconfigDTO);
    }

    /**
     * 修改探头信息
     * @param instrumentparamconfigCommand
     */
    public void editInstrumentparamconfig(InstrumentparamconfigCommand instrumentparamconfigCommand) {
        InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO()
                .setInstrumentparamconfigno(instrumentparamconfigCommand.getInstrumentparamconfigno())
                .setInstrumentno(instrumentparamconfigCommand.getInstrumentNo())
                .setInstrumentconfigid(instrumentparamconfigCommand.getInstrumentconfigid())
                .setInstrumentname(instrumentparamconfigCommand.getInstrumentname())
                .setLowlimit(instrumentparamconfigCommand.getLowlimit())
                .setHighlimit(instrumentparamconfigCommand.getHighlimit())
                .setInstrumenttypeid(instrumentparamconfigCommand.getInstrumenttypeid())
                .setWarningphone(instrumentparamconfigCommand.getWarningphone())
                .setChannel(instrumentparamconfigCommand.getChannel())
                .setSaturation(instrumentparamconfigCommand.getSaturation());
        instrumentparamconfigService.updateInfo(instrumentparamconfigDTO);
    }

    /**
     * 清除探头信息
     * @param instrumentparamconfigno
     */
    public void removeInstrumentparamconfig(String instrumentparamconfigno) {
        instrumentparamconfigService.deleteInfo(new InstrumentparamconfigDTO().setInstrumentparamconfigno(instrumentparamconfigno));
    }


    /**
     * 分页获取探头信息
     * @param instrumentparamconfigCommand
     * @return
     */
    public Page<InstrumentparamconfigVo> findInstrumentparamconfig(InstrumentparamconfigCommand instrumentparamconfigCommand) {
        Page page = new Page(instrumentparamconfigCommand.getPageCurrent(),instrumentparamconfigCommand.getPageSize());
        List<InstrumentparamconfigDTO> instrumentparamconfigDTOS =  instrumentparamconfigService.findInstrumentparamconfig(page,instrumentparamconfigCommand);
        List<InstrumentparamconfigVo> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(instrumentparamconfigDTOS)){

            for (InstrumentparamconfigDTO configDTO : instrumentparamconfigDTOS) {
                InstrumentparamconfigVo build = InstrumentparamconfigVo
                        .builder()
                        .hospitalname(configDTO.getHospitalname())
                        .hospitalCode(configDTO.getHospitalcode())
                        .equipmentName(configDTO.getEquipmentname())
                        .equipmenttypename(configDTO.getEquipmenttypename())
                        .instrumentname(configDTO.getInstrumentname())
                        .sn(configDTO.getSn())
                        .firsttime(configDTO.getFirsttime())
                        .channel(configDTO.getChannel())
                        .instrumentconfigname(configDTO.getInstrumentconfigname())
                        .alarmtime(configDTO.getAlarmtime())
                        .lowlimit(configDTO.getLowlimit())
                        .highlimit(configDTO.getHighlimit())
                        .saturation(configDTO.getSaturation())
                        .warningphone(configDTO.getWarningphone())
                        .build();
                list.add(build);
            }
        }
       page.setRecords(list);
        return page;
    }
}
