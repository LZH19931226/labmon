package com.hc.my.common.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 声光报警工具
 */
public class SoundLightUtils {
    /**
     * 关闭声光报警的指命
     */
    public static final String TURN_OFF_ROUND_LIGHT_COMMAND= "484316001D23";

    /**
     * 开启声光报警的指命
     */
    public static final String TURN_ON_ROUND_LIGHT_COMMAND = "484315001E23";

    /**
     * 获取cmdid
     * @param sn
     * @return
     */
    public static String getCmdId(String sn) {
        String subStr = sn.substring(4, 6);
        if(StringUtils.equals("11",subStr)){
            return "88";
        }
        return "a4";
    }

}
