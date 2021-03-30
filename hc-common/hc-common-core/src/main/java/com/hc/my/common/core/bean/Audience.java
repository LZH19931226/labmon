package com.hc.my.common.core.bean;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author LiuZhiHao
 * @date 2019/10/21 14:10
 * 描述:
 **/
@Data
@Configuration
public class Audience {
    private  String clientId ="098f6bcd4621d373cade4e832627b4f6";
    private  String base64Secret="MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY";
    private  String name="hc";
    private  int expiresSecond=(int)(Duration.ofMinutes(15).getSeconds()*1000);
}
