package com.hc.mapper.laboratoryFrom;

import com.hc.entity.Monitorinstrument;
import com.hc.entity.Repairinfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 15350 on 2020/5/27.
 */
@Component
public interface RepairinfoMapper {

    /**
     * 根据设备编号查询SN号
     * @param equipmentno
     * @return
     */
    @Select(" select * from monitorinstrument where equipmentno = #{equipmentno}")
    List<Monitorinstrument> getMonitorInstrumentSn(@Param("equipmentno") String equipmentno);


    /**
     * 分页查询维修记录
     * @param rowBounds
     * @param repairinfo
     * @return
     */
    List<Repairinfo> getPageInfo(RowBounds rowBounds, Repairinfo repairinfo);




}
