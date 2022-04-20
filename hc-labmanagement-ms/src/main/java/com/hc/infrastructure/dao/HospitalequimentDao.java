package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.dto.HospitalequimentDTO;
import com.hc.po.HospitalequimentPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.vo.hospital.HospitalInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */

public interface HospitalequimentDao extends BaseMapper<HospitalequimentPo> {

    List<HospitalequimentDTO> selectHospitalEquimentType(Page<HospitalInfoVo> page,@Param("hospitalEquimentTypeCommand") HospitalEquimentTypeCommand hospitalEquimentTypeCommand);
}
