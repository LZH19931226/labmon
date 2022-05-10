package com.hc.application;

import com.hc.dto.OperationlogdetailDTO;
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
            dto.forEach(res->{
                OperationlogdetailVo build = OperationlogdetailVo.builder()
                        .detailid(res.getDetailid())
                        .filedcaption(res.getFiledcaption())
                        .filedname(res.getFiledname())
                        .filedvalue(res.getFiledvalue())
                        .filedvalueprev(res.getFiledvalue())
                        .comment(res.getComment())
                        .build();
                list.add(build);
            });
        }
        return list;
    }
}
