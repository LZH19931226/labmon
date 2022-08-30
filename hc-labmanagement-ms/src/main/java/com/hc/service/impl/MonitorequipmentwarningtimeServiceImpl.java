package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.MonitorEquipmentWarningTimePo;
import com.hc.repository.MonitorequipmentwarningtimeRepository;
import com.hc.service.MonitorequipmentwarningtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorequipmentwarningtimeServiceImpl implements MonitorequipmentwarningtimeService {

    @Autowired
    private MonitorequipmentwarningtimeRepository monitorequipmentwarningtimeRepository;

    @Override
    public List<MonitorequipmentwarningtimeDTO> selectWarningtimeByHosCode(List<String> hospitalcodes) {
        List<MonitorEquipmentWarningTimePo> times = monitorequipmentwarningtimeRepository.list(Wrappers.lambdaQuery(new MonitorEquipmentWarningTimePo())
                .eq(MonitorEquipmentWarningTimePo::getEquipmentCategory, "TYPE")
                .in(MonitorEquipmentWarningTimePo::getHospitalCode, hospitalcodes));
        return BeanConverter.convert(times,MonitorequipmentwarningtimeDTO.class);
    }

    /**
     * 差如报警时段
     *
     * @param monitorequipmentwarningtimeDTO
     */
    @Override
    public void insetWarningtimeList(List<MonitorequipmentwarningtimeDTO> monitorequipmentwarningtimeDTO) {
        monitorequipmentwarningtimeRepository.insetWarningtimeList(monitorequipmentwarningtimeDTO);

    }


    /**
     * 批量修改报警时间段集合
     *
     * @param updateList
     */
    @Override
    public void updateList(List<MonitorequipmentwarningtimeDTO> updateList) {
        List<MonitorEquipmentWarningTimePo> convert = BeanConverter.convert(updateList, MonitorEquipmentWarningTimePo.class);
        monitorequipmentwarningtimeRepository.updateBatchById(convert);
    }

    /**
     * 删除报警时段
     *
     * @param monitorequipmentwarningtimeDTO
     */
    @Override
    public void deleteInfo(MonitorequipmentwarningtimeDTO monitorequipmentwarningtimeDTO) {
        monitorequipmentwarningtimeRepository.deleteInfo(monitorequipmentwarningtimeDTO);
    }

    /**
     * 获取报警时段集合
     *
     * @param hospitalCode
     * @param equipmentNo
     * @return
     */
    @Override
    public List<MonitorequipmentwarningtimeDTO> selectWarningtimeByHosCodeAndEno(String hospitalCode, String equipmentNo) {
        return monitorequipmentwarningtimeRepository.selectWarningtimeByHosCodeAndEno(hospitalCode,equipmentNo);
    }

    /**
     * 获取报警时段
     *
     * @param equipmentNo
     * @return
     */
    @Override
    public Integer selectWarningtimeByEno(String equipmentNo) {

        return monitorequipmentwarningtimeRepository.selectWarningtimeByEno(equipmentNo);
    }

    /**
     * 获取超时集合
     *
     * @param equipmentNoList
     * @return
     */
    @Override
    public List<MonitorequipmentwarningtimeDTO> selectWarningtimeByEnoList(List<String> equipmentNoList) {
        return monitorequipmentwarningtimeRepository.selectWarningtimeByEnoList(equipmentNoList);
    }


    @Override
    public List<MonitorequipmentwarningtimeDTO> selectWarningtimeByHospitalCode(List<String> hospitalCodes) {
        List<MonitorEquipmentWarningTimePo> times = monitorequipmentwarningtimeRepository.list(Wrappers.lambdaQuery(new MonitorEquipmentWarningTimePo())
                .eq(MonitorEquipmentWarningTimePo::getEquipmentCategory, "EQ")
                .in(MonitorEquipmentWarningTimePo::getHospitalCode, hospitalCodes));
        return BeanConverter.convert(times,MonitorequipmentwarningtimeDTO.class);
    }

    /**
     * 通过主键批量删除
     *
     * @param idList
     */
    @Override
    public void bulkRemove(List<Integer> idList) {
        monitorequipmentwarningtimeRepository.removeByIds(idList);
    }
}
