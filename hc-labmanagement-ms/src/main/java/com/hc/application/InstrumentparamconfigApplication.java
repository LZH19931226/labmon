package com.hc.application;

import com.hc.dto.InstrumentconfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.service.InstrumentparamconfigService;
import com.hc.vo.equimenttype.InstrumentparamconfigVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Component
public class InstrumentparamconfigApplication {

    @Autowired
    private InstrumentparamconfigService instrumentparamconfigService;


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
}
