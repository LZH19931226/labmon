package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.clickhouse.param.EquipmentDataParam;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.DateUtils;

public class SystemDataApplication {



    public Page findPacketLossLog(EquipmentDataCommand equipmentDataCommand) {
        String startTime = equipmentDataCommand.getStartTime();
        String ym = DateUtils.parseDateYm(startTime);
        equipmentDataCommand.setYearMonth(ym);
        EquipmentDataParam convert = BeanConverter.convert(equipmentDataCommand, EquipmentDataParam.class);
        return  null;
    }
}
