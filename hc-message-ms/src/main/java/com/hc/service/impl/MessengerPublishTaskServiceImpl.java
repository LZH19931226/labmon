package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.entity.MessengerPublishTask;
import com.hc.infrastructure.dao.MessengerPublishTaskMapper;
import com.hc.service.MessengerPublishTaskService;
import org.springframework.stereotype.Service;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 14:12
 * 描述:
 **/
@Service
public class MessengerPublishTaskServiceImpl extends ServiceImpl<MessengerPublishTaskMapper, MessengerPublishTask> implements MessengerPublishTaskService {
}
