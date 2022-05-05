package com.hc.mapper.laboratoryFrom;

import com.hc.po.UserScheduLing;
import com.hc.model.ResponseModel.UserAuthInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 16956 on 2018-08-06.
 */
@Mapper
@Component
public interface UserAuthorInfoMapper {

    /**
     * 分页模糊查询用户权限信息
     */
    List<UserAuthInfoModel> selectUserAuthorPage(RowBounds rowBounds);



    List<UserScheduLing> searchScByHosMon(@Param("hosId") String hosId, @Param("month") String month);

    List<UserScheduLing> searchScByHosMonSection(@Param("hosId") String hosId, @Param("startMonth") String startMonth, @Param("endMonth") String endMonth);
}
