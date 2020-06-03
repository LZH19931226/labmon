package com.hc.EventListener;

import com.netflix.appinfo.InstanceInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaRegistryAvailableEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
 
/**
 * Created by Liu
 */
@Component
public class EurekaStateChangeListener {
	
	private static final Logger log = LoggerFactory.getLogger(EurekaStateChangeListener.class);

    
    @EventListener
    public void listen(EurekaInstanceCanceledEvent eurekaInstanceCanceledEvent) {
        //服务断线事件
        String appName = eurekaInstanceCanceledEvent.getAppName();
        String serverId = eurekaInstanceCanceledEvent.getServerId();
       //可做服务下线推送给运维人员
        log.info("该服务从注册中心下线"+appName+"具体信息"+serverId);
    }
 
    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        log.info("该服务注册到注册中心"+instanceInfo.getAppName());
    }
 
    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
      
    }
 
    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
 
    }
 
    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        
    }
}