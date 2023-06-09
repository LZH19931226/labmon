package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.dto.SysNationalDto;
import com.hc.po.SysNationalPo;
import java.util.List;

/**
 * 国家信息库接口
 * @author hc
 */
public interface SysNationalRepository extends IService<SysNationalPo> {

    List<SysNationalDto> getNational();

}
