package com.hc.application.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmHand {

    /**
     * 启用数量
     */
    private Integer enableNum;

    /**
     * 禁用数量
     */
    private Integer disabledNum;
}
