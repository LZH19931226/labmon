package com.hc.mapper.laboratoryFrom;

import com.hc.model.ResponseModel.EquipmentConfigInfoModel;
import com.hc.model.ResponseModel.InstrumentParamConfigInfo;
import com.hc.model.ResponseModel.InstrumentParamConfigInfos;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 16956 on 2018-08-05.
 */
@Mapper
@Component
public interface InstrumentParamConfigInfoMapper {
    /**
     * 分页展示当前设备类型所有设备信息
     * @param equipmentConfigInfoModel
     * @return
     */
    List<InstrumentParamConfigInfos> ShowInsParamConfigInfos(RowBounds rowBounds,EquipmentConfigInfoModel equipmentConfigInfoModel);

    /**
     * 展示当前设备所有探头类型是否启用信息
     */
    List<InstrumentParamConfigInfo> ShowInsParamConfigInfo(String equipmentno);


}
