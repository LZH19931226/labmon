package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.InstrumentmonitorDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.infrastructure.dao.InstrumentmonitorDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.InstrumentmonitorPo;
import com.hc.repository.InstrumentmonitorRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
public class InstrumentmonitorRepositoryImpl extends ServiceImpl<InstrumentmonitorDao,InstrumentmonitorPo> implements InstrumentmonitorRepository {

    @Autowired
    private InstrumentmonitorDao instrumentmonitorDao;


    @Override
    public List<MonitorinstrumenttypeDTO> selectMonitorEquipmentType(String instrumenttypeid) {
       List<InstrumentmonitorDTO> instrumentmonitorDTOS  = instrumentmonitorDao.selectMonitorEquipmentType(instrumenttypeid);
       if (CollectionUtils.isNotEmpty(instrumentmonitorDTOS)){
           List<MonitorinstrumenttypeDTO> dtoList  = new ArrayList<>();
           Map<Integer, List<InstrumentmonitorDTO>> collect = instrumentmonitorDTOS.stream().collect(Collectors.groupingBy(InstrumentmonitorDTO::getInstrumenttypeid));
           collect.forEach((k,v)->{
               MonitorinstrumenttypeDTO monitorinstrumenttypeDTO = new MonitorinstrumenttypeDTO();
               monitorinstrumenttypeDTO.setInstrumenttypeid(k);
               monitorinstrumenttypeDTO.setInstrumenttypename(v.get(0).getInstrumenttypename());
               monitorinstrumenttypeDTO.setInstrumentmonitorDTOS(v);
               dtoList.add(monitorinstrumenttypeDTO);
            });
           return  dtoList;
       }
       return null;
    }

    /**
     * 插入监控仪器信息
     *
     * @param instrumentmonitorDTO
     */
    @Override
    public void insertInstrumentmonitorInfo(InstrumentmonitorDTO instrumentmonitorDTO) {
        InstrumentmonitorPo instrumentmonitorPo = BeanConverter.convert(instrumentmonitorDTO, InstrumentmonitorPo.class);
        instrumentmonitorDao.insert(instrumentmonitorPo);
    }

    /**
     * 更新监控仪器信息
     *
     * @param instrumentmonitorDTO
     */
    @Override
    public void updateInstrumentmonitor(InstrumentmonitorDTO instrumentmonitorDTO) {
        InstrumentmonitorPo instrumentmonitorPo = BeanConverter.convert(instrumentmonitorDTO, InstrumentmonitorPo.class);
        instrumentmonitorDao.update(instrumentmonitorPo,
                Wrappers.lambdaUpdate(new InstrumentmonitorPo())
                        .eq(InstrumentmonitorPo::getInstrumentconfigid,instrumentmonitorPo.getInstrumentconfigid())
                        .eq(InstrumentmonitorPo::getInstrumenttypeid,instrumentmonitorPo.getInstrumenttypeid()));
    }

    /**
     * 判断插入的信息是否已存在
     *
     * @param instrumentmonitorDTO
     * @return
     */
    @Override
    public boolean selectOne(InstrumentmonitorDTO instrumentmonitorDTO) {
        InstrumentmonitorPo instrumentmonitorPo = instrumentmonitorDao.selectOne(Wrappers.lambdaQuery(new InstrumentmonitorPo())
                .eq(InstrumentmonitorPo::getInstrumentconfigid, instrumentmonitorDTO.getInstrumentconfigid())
                .eq(InstrumentmonitorPo::getInstrumenttypeid, instrumentmonitorDTO.getInstrumenttypeid()));
        return instrumentmonitorPo == null ? false : true;
    }

    /**
     * 查询监控仪器信息
     *
     * @param instrumenttypeid
     * @return
     */
    @Override
    public List<InstrumentmonitorDTO> selectMonitorEquipmentList(Integer instrumenttypeid) {
        List<InstrumentmonitorPo> instrumentmonitorPos = instrumentmonitorDao.selectList(Wrappers.lambdaQuery(new InstrumentmonitorPo())
                .eq(InstrumentmonitorPo::getInstrumenttypeid, instrumenttypeid));
        return BeanConverter.convert(instrumentmonitorPos,InstrumentmonitorDTO.class);
    }

    /**
     * 查询所有探头默认信息
     */
    @Override
    public List<InstrumentmonitorDTO> selectMonitorEquipmentAll() {
        List<InstrumentmonitorPo> instrumentmonitorPos = instrumentmonitorDao.selectList(null);
        return BeanConverter.convert(instrumentmonitorPos,InstrumentmonitorDTO.class);
    }

    /**
     * 查询探头监控的信息
     *
     * @return
     */
    @Override
    public List<InstrumentmonitorDTO> selectInstrumentMonitorInfo(String hospitalCode) {
        return instrumentmonitorDao.selectInstrumentMonitorInfo(hospitalCode);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param instrumentTypeId
     * @return
     */
    @Override
    public List<InstrumentmonitorDTO> listByPage(Page<InstrumentmonitorDTO> page, Integer instrumentTypeId,Integer instrumentConfigId) {
        return instrumentmonitorDao.listByPage(page,instrumentTypeId,instrumentConfigId);
    }
}
