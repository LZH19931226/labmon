package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.MonitorequipmenttypeDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.infrastructure.dao.MonitorinstrumenttypeDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.MonitorinstrumenttypePo;
import com.hc.repository.MonitorinstrumenttypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 * @author hc
 */
@Repository
public class MonitorinstrumenttypeRepositoryImpl extends ServiceImpl<MonitorinstrumenttypeDao,MonitorinstrumenttypePo> implements MonitorinstrumenttypeRepository  {

    @Autowired
    private MonitorinstrumenttypeDao monitorinstrumenttypeDao;
    /**
     * 查询监控设备类型信息
     *
     * @param instrumenttypeid@return
     */
    @Override
    public MonitorequipmenttypeDTO selectinfoByTypeid(Integer instrumenttypeid) {
        MonitorinstrumenttypePo monitorinstrumenttypePo =
                monitorinstrumenttypeDao.selectOne(Wrappers.lambdaQuery(new MonitorinstrumenttypePo()).eq(MonitorinstrumenttypePo::getInstrumenttypeid, instrumenttypeid));
        return BeanConverter.convert(monitorinstrumenttypePo,MonitorequipmenttypeDTO.class);
    }

    /**
     * 查询所有的监控设备类型信息
     *
     * @return
     */
    @Override
    public List<MonitorinstrumenttypeDTO> seleclAll() {
        List<MonitorinstrumenttypePo> monitorinstrumenttypePos = monitorinstrumenttypeDao.selectList(null);
        return BeanConverter.convert(monitorinstrumenttypePos,MonitorinstrumenttypeDTO.class);
    }
}
