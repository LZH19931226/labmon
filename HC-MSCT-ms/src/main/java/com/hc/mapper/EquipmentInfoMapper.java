//package com.hc.mapper;
//
//import com.hc.entity.Monitorequipment;
//import com.hc.model.ExcleInfoModel.EnvironmentListModel;
//import com.hc.model.ExcleInfoModel.IncubatorListModel;
//import com.hc.model.ExcleInfoModel.OtherListModel;
//import com.hc.model.Params;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Select;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * Created by 16956 on 2018-08-01.
// */
//@Mapper
//@Component
//public interface EquipmentInfoMapper {
//
//
//    @Select("select * from monitorequipment where equipmentno = #{equipmentno}")
//    Monitorequipment getEquipmentByNo(@Param("equipmentno") String equipmentno);
//    /**
//     * 查询环境数据    excle 导出
//     */
//    EnvironmentListModel getHJExcleInfo(Params curvelReqeustModel);
//
//    List<EnvironmentListModel> getHJExcleInfoById(Params curvelReqeustModel);
//
//    /**
//     * 查询培养箱数据  excle导出
//     */
//    IncubatorListModel getPyxExcleInfo(Params curvelReqeustModel);
//    List<IncubatorListModel> getPyxExcleInfoById(Params curvelReqeustModel);
//    /**
//     * 查询其他数据 excle导出
//     */
//    OtherListModel getOtherExcleInfo(Params curvelReqeustModel);
//    List<OtherListModel> getOtherExcleInfoById(Params curvelReqeustModel);
//
//}
