package com.hc.listenter;

import com.hc.bean.WarningMqModel;
import com.hc.entity.Monitorinstrument;
import com.hc.exchange.SocketMessage;
import com.hc.my.common.core.bean.ParamaterModel;
import com.hc.service.InstrumentMonitorInfoService;
import com.hc.service.MTJudgeService;
import com.hc.service.MessagePushService;
import com.hc.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableBinding(SocketMessage.class)
public class SocketMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketMessageListener.class);
	@Autowired
	private MessagePushService service;
	@Autowired
	private MTJudgeService mtJudgeService;
	@Autowired
	private InstrumentMonitorInfoService instrumentMonitorInfoService;

	
	
	
	@StreamListener(SocketMessage.EXCHANGE_NAME)
	public void onMessage(String messageContent){
		ParamaterModel model = JsonUtil.toBean(messageContent, ParamaterModel.class);
		LOGGER.info("数据处理中心服务接收到队列1中数据："+JsonUtil.toJson(model));
		//MT500  MT600判断
		Monitorinstrument monitorinstrument = mtJudgeService.mtJudge(model);
		if (monitorinstrument==null) {
			return ;
		}
		//执行数据写入 、 报警推送
		List<WarningMqModel> save = instrumentMonitorInfoService.save(model, monitorinstrument);
		//报警消息处理
		if(CollectionUtils.isNotEmpty(save)){
			for(WarningMqModel warningModel : save){
				service.pushMessage1(JsonUtil.toJson(warningModel));
				LOGGER.info("数据插入服务结束，推送去报警服务："+JsonUtil.toJson(warningModel));

			}
		}

		
	}


	@StreamListener(SocketMessage.EXCHANGE_NAME2)
	public void onMessage1(String messageContent){
		ParamaterModel model = JsonUtil.toBean(messageContent, ParamaterModel.class);
		LOGGER.info("数据处理中心服务接收到队列2中数据："+JsonUtil.toJson(model));
		//MT500  MT600判断
		Monitorinstrument monitorinstrument = mtJudgeService.mtJudge(model);
		if (monitorinstrument==null) {
			return ;
		}
		//执行数据写入 、 报警推送
		List<WarningMqModel> save = instrumentMonitorInfoService.save(model, monitorinstrument);
		//报警消息处理
		if(CollectionUtils.isNotEmpty(save)){
			for(WarningMqModel warningModel : save){
				service.pushMessage2(JsonUtil.toJson(warningModel));
				LOGGER.info("数据插入服务结束，推送去报警服务："+JsonUtil.toJson(warningModel));

			}
		}


	}


	@StreamListener(SocketMessage.EXCHANGE_NAME3)
	public void onMessage2(String messageContent){
		ParamaterModel model = JsonUtil.toBean(messageContent, ParamaterModel.class);
		LOGGER.info("数据处理中心服务接收到队列3中数据："+JsonUtil.toJson(model));
		//MT500  MT600判断
		Monitorinstrument monitorinstrument = mtJudgeService.mtJudge(model);
		if (monitorinstrument==null) {
			return ;
		}
		//执行数据写入 、 报警推送
		List<WarningMqModel> save = instrumentMonitorInfoService.save(model, monitorinstrument);
		//报警消息处理
		if(CollectionUtils.isNotEmpty(save)){
			for(WarningMqModel warningModel : save){
				service.pushMessage3(JsonUtil.toJson(warningModel));
				LOGGER.info("数据插入服务结束，推送去报警服务："+JsonUtil.toJson(warningModel));

			}
		}


	}
	
}
