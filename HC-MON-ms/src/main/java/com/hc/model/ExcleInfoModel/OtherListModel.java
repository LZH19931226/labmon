package com.hc.model.ExcleInfoModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by xxf on 2018-12-25.
 */
@Getter
@Setter
@ToString
public class OtherListModel {

    private String equipmentname;

    private List<OtherModel> otherModels;
}
