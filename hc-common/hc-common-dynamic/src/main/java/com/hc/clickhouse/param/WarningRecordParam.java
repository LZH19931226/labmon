package com.hc.clickhouse.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WarningRecordParam {

    private String hospitalCode;

    private String equipmentNo;

    private String startTime;

    private String endTime;

}
