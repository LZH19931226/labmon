package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.InstrumentconfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.po.InstrumentparamconfigPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Mapper
public interface InstrumentparamconfigDao extends BaseMapper<InstrumentparamconfigPo> {


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
     * @param equipmentTypeName
     * @param instrumentname
     * @param sn
     * @return
     */
    List<InstrumentparamconfigDTO> findInstrumentparamconfig(Page page,
                                                             @Param("hospitalCode") String hospitalCode,
                                                             @Param("equipmentTypeName") String equipmentTypeName,
                                                             @Param("instrumentname") String instrumentname,
                                                             @Param("sn") String sn);
}
