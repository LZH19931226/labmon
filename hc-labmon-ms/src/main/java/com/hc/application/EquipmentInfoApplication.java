package com.hc.application;

import com.hc.command.labmanagement.model.HospitalEquipmentTypeModel;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.hospital.HospitalInfoApi;
import com.hc.labmanagent.HospitalEquipmentTypeApi;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.my.common.core.struct.Context;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;


/**
 * @author hc
 */
@Component
public class EquipmentInfoApplication {

    @Autowired
    private HospitalInfoApi hospitalInfoApi;

    @Autowired
    private HospitalEquipmentTypeApi hospitalEquipmentTypeApi;


    /**
     * 获取医院信息
     * @param hospitalCode
     * @return
     */
    public HospitalMadel getHospitalInfO(String hospitalCode) {
        HospitalMadel hospitalInfo = hospitalInfoApi.findHospitalInfo(hospitalCode).getResult();
        if(ObjectUtils.isEmpty(hospitalInfo)){
            return null;
        }
        String timeoutRedDuration = hospitalInfo.getTimeoutRedDuration();
        if (StringUtils.isEmpty(timeoutRedDuration)) {
            hospitalInfo.setTimeoutRedDuration("60");
        }
        List<HospitalEquipmentTypeModel> hospitalEquipmentTypeModelList = hospitalEquipmentTypeApi.findHospitalEquipmentTypeByCode(hospitalCode).getResult();
        if(CollectionUtils.isEmpty(hospitalEquipmentTypeModelList)){
            throw new IedsException(LabSystemEnum.HOSPITAL_IS_NOT_BOUND_EQUIPMENT_TYPE);
        }
        String lang = Context.getLang();
        if("en".equals(lang)){
            hospitalEquipmentTypeModelList.forEach(res->{
                res.setEquipmentTypeName(res.getEquipmentTypeNameUS());
            });
        }
        hospitalInfo.setHospitalEquipmentTypeModelList(hospitalEquipmentTypeModelList);
        return hospitalInfo;
    }
}
