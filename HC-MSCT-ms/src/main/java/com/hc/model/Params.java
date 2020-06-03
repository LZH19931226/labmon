package com.hc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by xxf on 2018-12-26.
 */
@Getter
@Setter
@ToString
public class Params {

    private String equipmenttypeid;

    private String equipmentno;

    private String hospitalcode;

    private String startdate;

    private String enddate;

    private String search;

    private String type;

    private HttpServletResponse response;

}
