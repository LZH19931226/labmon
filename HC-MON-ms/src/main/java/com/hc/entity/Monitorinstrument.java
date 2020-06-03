package com.hc.entity;

import com.hc.model.CurrentInfoModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
@Getter
@Setter
@ToString
public class Monitorinstrument implements Serializable {
    private String instrumentno;

    private String instrumentname;

    private String equipmentno;

    private String hospitalcode;

    private Integer instrumenttypeid;

    private String sn;

    private static final long serialVersionUID = 1L;

    private CurrentInfoModel currentInfoModel;

    private Map<String,Object> map;


}