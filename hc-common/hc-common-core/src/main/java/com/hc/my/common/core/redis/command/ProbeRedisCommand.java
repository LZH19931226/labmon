package com.hc.my.common.core.redis.command;

import lombok.Data;

import java.util.List;

@Data
public class ProbeRedisCommand {

    private String hospitalCode;

    private List<String> eNoList;
}
