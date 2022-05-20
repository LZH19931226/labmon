package com.hc.my.common.core.redis.command;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class EquipmentInfoCommand {
    private String hospitalCode;
    private List<String> equipmentNoList;
}
