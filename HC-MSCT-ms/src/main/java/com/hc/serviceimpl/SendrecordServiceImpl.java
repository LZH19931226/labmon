package com.hc.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.mapper.SendrecordDao;
import com.hc.po.Sendrecord;
import com.hc.service.SendrecordService;
import org.springframework.stereotype.Service;

@Service
public class SendrecordServiceImpl extends ServiceImpl<SendrecordDao, Sendrecord> implements SendrecordService {
}
