package com.hc.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

@Data
@Accessors(chain = true)
public class MonitorinstrumentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String instrumentno;

    private String instrumentname;

    private String equipmentno;

    private String hospitalcode;

    private Integer instrumenttypeid;

    private String sn;

    private String lowlimit;

    private CurrentInfoDto currentInfoDto;

    private Map<String,Object> map;

    private String channel;
}
