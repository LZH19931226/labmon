package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.po.MonitorinstrumenttypePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface MonitorinstrumenttypeDao extends BaseMapper<MonitorinstrumenttypePo> {

    List<MonitorinstrumenttypeDTO> list(Page page, @Param("instrumentTypeName") String instrumentTypeName);
}
