package com.hc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabHosWarningTimeDto {

    private Long id;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private String hospitalCode;

}
