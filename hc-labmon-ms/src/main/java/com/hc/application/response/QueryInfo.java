package com.hc.application.response;

import com.hc.clickhouse.po.Monitorequipmentlastdata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryInfo {
    private  List<String> fieldList;
    private  List<Monitorequipmentlastdata> monitorEquipmentLastDataList;
}
