package com.hc.mapper.laboratoryFrom;

import com.hc.po.Monitorequipment;
import com.hc.model.MapperModel.PageUserModel;
import com.hc.model.ResponseModel.MonitorEquipmentInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 16956 on 2018-08-07.
 *
 */
@Mapper
@Component
public interface MonitorEquipmentMapper {

    /**
     * 分页模糊查询设备信
     */
    List<MonitorEquipmentInfoModel> selectEquipmentPage(RowBounds rowBounds, PageUserModel pageUserModel);

    /**
     * 根据医院编号和设备类型编号查询设备信息
     */
    @Select("select * from monitorequipment where hospitalcode = #{hospitalcode} and equipmenttypeid = #{equipmenttypeid} order by" +
            " equipmentname ")
    List<Monitorequipment> selectEquipmentByCode(@Param("hospitalcode") String hospitalcode, @Param("equipmenttypeid") String equipmenttypeid);

    /**
     * updateEquipmentName
     */
    @Select(" update monitorequipment set sort = #{sort} where equipmentno = #{equipmentno}")
    void updateEquipmentSort(@Param("sort") int sort ,@Param("equipmentno") String equipmentno);
}
