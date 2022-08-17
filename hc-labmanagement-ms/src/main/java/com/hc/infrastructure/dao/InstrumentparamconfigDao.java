package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.InstrumentconfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.po.InstrumentparamconfigPo;
import com.hc.vo.equimenttype.InstrumentparamconfigVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface InstrumentparamconfigDao extends RootMapper<InstrumentparamconfigPo> {


    /**
     * 通过设备no获取设备对应的检测信息
     * @param equipmentNo
     * @return
     */
    List<InstrumentconfigDTO> selectInstrumentparamconfigByEqNo(@Param("equipmentNo") String equipmentNo);

    /**
     * 分页查询探头参数配置信息
     * @param page
     * @param hospitalCode
     * @param equipmentTypeId
     * @param instrumentNo
     * @param sn
     * @return
     */
    List<InstrumentparamconfigDTO> findInstrumentparamconfig(Page<InstrumentparamconfigVo> page,
                                                             @Param("hospitalCode") String hospitalCode,
                                                             @Param("equipmentTypeId") String equipmentTypeId,
                                                             @Param("instrumentNo") String instrumentNo,
                                                             @Param("sn") String sn);

    /**
     * 批量删除探头信息
     * @param instrumentParamConfigNos
     */
    void deleteInfos(String[] instrumentParamConfigNos);

    List<InstrumentparamconfigDTO> getInstrumentParamConfigInfo(String equipmentNo);
}
