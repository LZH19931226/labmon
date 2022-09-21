package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.InstrumentConfigDTO;
import com.hc.po.InstrumentconfigPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface InstrumentConfigDao extends BaseMapper<InstrumentconfigPo> {

    /**
     * 分页查询监控参数类型信息
     * @param page
     * @param instrumentConfigName
     * @return
     */
    List<InstrumentConfigDTO> listByPage(Page<InstrumentConfigDTO> page,@Param("instrumentConfigName") String instrumentConfigName);
}
