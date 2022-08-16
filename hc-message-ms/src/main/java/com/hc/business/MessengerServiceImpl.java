package com.hc.business;

import com.hc.entity.MessengerPublishTask;
import com.hc.entity.MessengerServiceDefine;
import com.hc.mapper.MessengerServiceDefineMapper;
import com.hc.model.NotifyMessage;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.struct.Maps;
import com.hc.my.common.core.util.UniqueHash;
import com.hc.executor.NotifyExecutor;
import com.hc.service.MessengerPublishTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 13:58
 * 描述:
 **/
@Service
public class MessengerServiceImpl implements MessengerService {


    @Resource
    private NotifyExecutor notifyExecutor;
    @Resource
    private MessengerServiceDefineMapper serviceDefineDao;
    @Resource
    private MessengerPublishTaskService publishTaskDao;


    @Override
    public String send(NotifyMessage notify) {
        //1、获取消息服务类型
        MessengerServiceDefine define = getServiceDefine(notify.getServiceNo());
        //2、生成并保存消息发布任务
        String batchNo = UniqueHash.Id();
        List<MessengerPublishTask> tasks = notify
                .getPrincipals()
                .entrySet()
                .stream()
                .flatMap(entry -> notify.getChannels().stream().map(channel -> {
                    MessengerPublishTask task = new MessengerPublishTask();
                    task.setBatchNo(batchNo);
                    task.setConsumerId(entry.getKey());
                    task.setCreateBy("admin");
                    task.setCreateTime(LocalDateTime.now());
                    task.setDelFlag(false);
                    task.setFailureTimes(0);
//                    if (NotifyChannel.SMS == channel) {
//                        task.setPublishKey(entry.getValue().toString());
//                        task.setMessageBodys(JSON.toJSONString(new Maps("tpl",
//                                define.getBusinessTpl(),
//                                "args",
//                                notify.getParams())));
//                    } else {
                    task.setPublishKey(entry.getKey());
                    task.setMessageBodys(notify.getMessageBodys());
                    task.setMessageCover(notify.getMessageCover());
                    task.setMessageIntro(notify.getMessageIntro());
                    task.setMessageTitle(notify.getMessageTitle());
                    task.setPublishTime(LocalDateTime.now());
                    task.setPublishType(channel.getCode());
                    task.setRemark("");
                    task.setServiceNo(notify.getServiceNo());
                    task.setStatus(0L);
                    task.setSupplierId("admin");
                    task.setTaskNo(UniqueHash.Id());
                    task.setUpdateBy("admin");
                    task.setUpdateTime(LocalDateTime.now());
                    return task;
                }))
                .collect(Collectors.toList());
        publishTaskDao.saveBatch(tasks);
        //3、根据消息服务号推送信息
        notifyExecutor.submit(batchNo);
        return batchNo;
    }


    private MessengerServiceDefine getServiceDefine(String serviceNo) {
        List<MessengerServiceDefine> defines = serviceDefineDao.selectByMap(new Maps("service_no", serviceNo));
        Optional<MessengerServiceDefine> define = Optional.ofNullable(defines).flatMap(fs -> fs.stream().findFirst());
        return define.orElseThrow(() -> new IedsException("服务号{}不存在!", serviceNo));
    }
}

