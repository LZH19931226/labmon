package com.hc.mapper.laboratoryFrom;

import com.hc.po.Userright;
import com.hc.model.MapperModel.PageUserModel;
import com.hc.model.ResponseModel.ClientInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 16956 on 2018-08-06.
 */
@Mapper
@Component
public interface ClientInfoMapper {

    /**
     * 根据用户id或者用户名查询
     */
    @Select("select * FROM userright where   username =#{username}")
    Userright selectUserByUser(Userright userright);
    /**
     * 根据用户id或者用户名查询
     */
    @Select("select * FROM userright where   userid =#{userid}")
    Userright selectUserByUserId(@Param("userid") String userright);
    /**
     * 分页模糊条件查询所有用户信息
     * @param rowBounds
     * @param pageUserModel
     * @return
     */
    List<ClientInfoModel> selectUserInfoPage(RowBounds rowBounds, PageUserModel pageUserModel);

    @Select("select * from userright where hospitalcode  =#{hospitalcode}")
    List<Userright> selectUserInfoByHospitalcode(@Param("hospitalcode") String hospitalcode);


}

