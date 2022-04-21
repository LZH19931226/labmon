package com.hc.config;

import com.hc.infrastructure.dao.HospitalofreginfoDao;
import com.hc.entity.Hospitalofreginfo;
import com.hc.entity.Userright;
import com.hc.mapper.laboratoryFrom.ClientInfoMapper;
import com.hc.units.JsonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(value = 3)
public class PoliceStationInfoCache implements CommandLineRunner {

    @Autowired
    private ClientInfoMapper clientInfoMapper;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Autowired
    private HospitalofreginfoDao hospitalofreginfoDao;

    private final Logger log = LoggerFactory.getLogger(PoliceStationInfoCache.class);

    @Override
    public void run(String... arg0) {
        try {
            if (redisTemplateUtil.hasKey("hospital:phonenum")) {
                redisTemplateUtil.delete("hospital:phonenum");
            }

            if (redisTemplateUtil.hasKey("hospital:info")) {
                redisTemplateUtil.delete("hospital:info");
            }


        } catch (Exception e) {
            log.error("删除redis缓存失败，原因：" + e.getMessage());
        }


        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();

        List<Hospitalofreginfo> hospitalofreginfoList;
        hospitalofreginfoList = hospitalofreginfoDao.findAll();
        for (Hospitalofreginfo hospitalofreginfo : hospitalofreginfoList) {
            List<Userright> userrightList;
            userrightList = clientInfoMapper.selectUserInfoByHospitalcode(hospitalofreginfo.getHospitalcode());
            if (CollectionUtils.isNotEmpty(userrightList)) {
                objectObjectObjectHashOperations.put("hospital:phonenum", hospitalofreginfo.getHospitalcode(), JsonUtil.toJson(userrightList));
            }
            objectObjectObjectHashOperations.put("hospital:info", hospitalofreginfo.getHospitalcode(), JsonUtil.toJson(hospitalofreginfo));
        }
        log.info("执行探头信息同步");
    }

}
