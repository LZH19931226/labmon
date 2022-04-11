package com.hc.my.common.core.probe;

import lombok.Data;

/**
 * MT600
 * @author hc
 */
@Data
public class MT600 extends PublicMethod{

    /**ups*/
    private String ups;

    /**
     * 获取ups的信息
     * @param upsCode ups码
     * @return
     */
    public String getUpsCodeInfO(String upsCode){
        return getUpsStateInfo(upsCode);
    }
}
