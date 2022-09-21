package com.hc.constants.error;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */

@Getter
@AllArgsConstructor
public enum InstrumentConfigEnumErrorCode {
    NAME_ALREADY_EXISTS("监控参数类型名称已存在");
    private String message;
}
