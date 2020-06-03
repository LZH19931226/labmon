package com.hc.model.ExcleInfoModel;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xxf on 2019-01-04.
 */
@ApiModel("培养箱导出模型")
@Getter
@Setter
@ToString
@Component
public class IncubatorListModel {


    private String equipmentname;


    private List<IncubatorModel> incubatorModelList;
}
