package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.po.MonitorEquipmentPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 监控设备
 * @author hc
 */
@Mapper
public interface MonitorEquipmentDao extends BaseMapper<MonitorEquipmentPo> {
    /**
     * 分页查询设信息
     * @param page 分页对象
     * @param hospitalCode 医院编码
     * @param equipmentTypeId 设备类型id
     * @param equipmentName 设备名称
     * @param clientVisible 是否启用
     * @return
     */
//    @Select("<script>" +
//            "SELECT  " +
//            "me.equipmentno equipmentNo, " +
//            "a.channel channel," +
//            "h.hospitalname hospitalName, " +
//            "h.hospitalcode hospitalCode," +
//            "met.equipmenttypename equipmentTypeName, " +
//            "met.equipmenttypeid equipmentTypeId ," +
//            "me.equipmentname equipmentName , " +
//            "a.sn, " +
//            "a.instrumentno instrumentNo," +
//            "a.instrumenttypeid instrumentTypeId," +
//            "a.instrumenttypename instrumentTypeName," +
//            "me.equipmentbrand equipmentBrand, " +
//            "me.clientvisible clientVisible, " +
//            "me.alwayalarm alwaysAlarm " +
//            "from  " +
//            "monitorequipment me " +
//            "LEFT JOIN hospitalofreginfo h ON me.hospitalcode = h.hospitalcode " +
//            "LEFT JOIN monitorequipmenttype met ON met.equipmenttypeid = me.equipmenttypeid " +
//            "LEFT JOIN (SELECT mi.equipmentno,mi.sn,mi.instrumentno,mi.channel,mit.instrumenttypename,mi.instrumenttypeid FROM monitorinstrument mi left JOIN monitorinstrumenttype mit ON mi.instrumenttypeid = mit.instrumenttypeid) a on a.equipmentno = me.equipmentno " +
//            "where 1=1 " +
//            "<if test = 'hospitalCode != null and hospitalCode != \"\"' > " +
//            "and h.hospitalcode = #{hospitalCode} " +
//            "</if>" +
//            "<if test = 'equipmentTypeId != null and equipmentTypeId != \"\"'> " +
//            "and met.equipmenttypeid = #{equipmentTypeId} " +
//            "</if> " +
//            "<if test ='equipmentName != null and equipmentName != \"\" '>" +
//            "and me.equipmentname like concat('%',#{equipmentName},'%') " +
//            "</if>" +
//            "<if test='clientVisible != null'> " +
//            "and me.clientvisible = #{clientVisible} " +
//            "</if>" +
//            "</script>")
    List<MonitorEquipmentDto> getEquipmentInfoList(Page page, @Param("hospitalCode") String hospitalCode,
                                                   @Param("equipmentTypeId") String equipmentTypeId,
                                                   @Param("equipmentName") String equipmentName,
                                                   @Param("clientVisible") Long clientVisible);
}
