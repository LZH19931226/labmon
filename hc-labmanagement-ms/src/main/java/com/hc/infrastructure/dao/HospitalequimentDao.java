package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.dto.HospitalequimentDTO;
import com.hc.po.HospitalequimentPo;
import com.hc.vo.equimenttype.HospitalequimentVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */

public interface  HospitalequimentDao extends BaseMapper<HospitalequimentPo> {

    List<HospitalequimentDTO> selectHospitalEquimentType(Page<HospitalequimentVo> page, @Param("hospitalEquimentTypeCommand") HospitalEquimentTypeCommand hospitalEquimentTypeCommand);

    /**
     * 查询医院设备信息
     * @param hospitalCode 医院code
     * @return
     */
    @Select("SELECT het.equipmenttypeid,het.equipmenttypename,hor.hospitalname,he.orderno,het.equipmenttypename_us FROM hospitalequiment he LEFT JOIN monitorequipmenttype het " +
            "ON het.equipmenttypeid = he.equipmenttypeid  LEFT JOIN hospitalofreginfo hor ON hor.hospitalcode = he.hospitalcode  WHERE he.hospitalcode = #{hospitalCode}")
    List<HospitalequimentDTO> selectHospitalEquipmentInfo(String hospitalCode);

    List<HospitalequimentDTO> getAllHospitalEquipmentTypeInfo();
}
