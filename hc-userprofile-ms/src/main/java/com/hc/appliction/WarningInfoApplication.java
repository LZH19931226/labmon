package com.hc.appliction;

import com.hc.dto.WarningrecordDto;
import com.hc.service.WarningrecordService;
import com.hc.vo.waring.WarningrecordVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WarningInfoApplication {

    @Autowired
    private WarningrecordService warningrecordService;


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
}
