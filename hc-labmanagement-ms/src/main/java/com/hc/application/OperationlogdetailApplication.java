package com.hc.application;

import com.hc.dto.OperationlogdetailDTO;
import com.hc.my.common.core.constant.enums.FeildEnum;
import com.hc.my.common.core.struct.Context;
import com.hc.service.OperationlogdetailService;
import com.hc.vo.backlog.OperationlogdetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 系统操作日志详细表
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Component
public class OperationlogdetailApplication {

    @Autowired
    private OperationlogdetailService operationlogdetailService;

    /**
     * 获取详细日志
     * @param logId
     * @return
     */
    public List<OperationlogdetailVo> getDetailedLogById(String logId) {
        List<OperationlogdetailDTO> dto =  operationlogdetailService.getDetailedLogById(logId);
        List<OperationlogdetailVo> list = new ArrayList<>();
        if (!ObjectUtils.isEmpty(dto)) {
            String lang = Context.getLang();
            dto.forEach(res->{
                OperationlogdetailVo build = OperationlogdetailVo.builder()
                        .detailid(res.getDetailid())
                        .filedcaption(editFiledCaption(res.getFiledcaption(),lang))
                        .filedname(res.getFiledname())
                        .filedvalue(res.getFiledvalue())
                        .filedvalueprev(res.getFiledvalueprev())
                        .comment(res.getComment())
                        .build();
                list.add(build);
            });
        }
        return list;
    }

    public String editFiledCaption(String message,String lang){
        if("en".equals(lang)){
            FeildEnum from = FeildEnum.from(message);
            return from.name();
        }
        return message;
    }
}
