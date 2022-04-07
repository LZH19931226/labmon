package com.hc.model;

import com.hc.entity.Monitorinstrument;
import lombok.Data;

@Data
public class MonitorinstrumentModel extends Monitorinstrument {

    /**
     * 是否显示
     */
    private Boolean clientvisible;

}
