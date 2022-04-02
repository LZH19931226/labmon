package com.hc.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by xxf on 2018-12-26.
 */
@Data
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

    public Params() {
    }

    public Params(String hospitalcode, String startdate, String enddate, String search) {
        this.hospitalcode = hospitalcode;
        this.startdate = startdate;
        this.enddate = enddate;
        this.search = search;
    }

}
