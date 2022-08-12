package com.hc.application.command;

import lombok.Data;

import java.util.List;

@Data
public class AlarmSystemCommand {

    private String warningPhone;

    private List<String> instrumentParamConfigNoList;

}
