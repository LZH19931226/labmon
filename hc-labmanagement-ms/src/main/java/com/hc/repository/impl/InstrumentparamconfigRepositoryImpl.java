package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.dto.InstrumentConfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.infrastructure.dao.InstrumentparamconfigDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.InstrumentparamconfigPo;
import com.hc.repository.InstrumentparamconfigRepository;
import com.hc.vo.equimenttype.InstrumentparamconfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public class InstrumentparamconfigRepositoryImpl extends ServiceImpl<InstrumentparamconfigDao,InstrumentparamconfigPo> implements InstrumentparamconfigRepository {

    @Autowired
    private InstrumentparamconfigDao instrumentparamconfigDao;

    /**
     * 插入探头配置信息
     *
     * @param instrumentparamconfigDTO
     */
    @Override
    public void insertInstrumentmonitor(InstrumentparamconfigDTO instrumentparamconfigDTO) {
        InstrumentparamconfigPo instrumentparamconfigPo = BeanConverter.convert(instrumentparamconfigDTO, InstrumentparamconfigPo.class);
        instrumentparamconfigDao.insert(instrumentparamconfigPo);
    }

    /**
     * 获取仪器信息集合
     *
     * @param instrumentNos
     * @return
     */
    @Override
    public List<InstrumentparamconfigDTO> slectinfo(List<String> instrumentNos) {

        List<InstrumentparamconfigPo> instrumentparamconfigPoList = instrumentparamconfigDao.selectList(Wrappers.lambdaQuery(new InstrumentparamconfigPo()).in(InstrumentparamconfigPo::getInstrumentno, instrumentNos));

        return BeanConverter.convert(instrumentparamconfigPoList,InstrumentparamconfigDTO.class);
    }

    /**
     * 删除探头参数信息
     *
     * @param instrumentno
     */
    @Override
    public void deleteInfoByEno(String instrumentno) {
        instrumentparamconfigDao.delete(Wrappers.lambdaQuery(new InstrumentparamconfigPo()).eq(InstrumentparamconfigPo::getInstrumentno,instrumentno));
    }

    @Override
    public List<InstrumentConfigDTO> selectInstrumentparamconfigByEqNo(String equipmentNo) {
        return instrumentparamconfigDao.selectInstrumentparamconfigByEqNo(equipmentNo);
    }

    /**
     * 更新探头配置信息
     *
     * @param instrumentparamconfigDTO
     */
    @Override
    public void updateInfo(InstrumentparamconfigDTO instrumentparamconfigDTO) {
        InstrumentparamconfigPo instrumentparamconfigPo = BeanConverter.convert(instrumentparamconfigDTO, InstrumentparamconfigPo.class);
        instrumentparamconfigDao.updateById(instrumentparamconfigPo);
    }

    /**
     * 删除探头信息
     *
     * @param instrumentparamconfigno
     */
    @Override
    public void deleteInstrumentparamconfig(InstrumentparamconfigDTO instrumentparamconfigno) {
        InstrumentparamconfigPo instrumentparamconfigPo = BeanConverter.convert(instrumentparamconfigno, InstrumentparamconfigPo.class);
        instrumentparamconfigDao.deleteById(instrumentparamconfigPo);
    }

    /**
     * 分页获取探头信息
     *
     * @param page
     * @param instrumentparamconfigCommand
     * @return
     */
    @Override
    public List<InstrumentparamconfigDTO> findInstrumentparamconfig(Page<InstrumentparamconfigVo> page, InstrumentparamconfigCommand instrumentparamconfigCommand) {
        return instrumentparamconfigDao.findInstrumentparamconfig(page,instrumentparamconfigCommand.getHospitalCode(),instrumentparamconfigCommand.getEquipmentTypeId(),
                                                                    instrumentparamconfigCommand.getEquipmentNo(), instrumentparamconfigCommand.getSn());

    }

    /**
     * 查询探头信息是否已存在
     *
     * @param instrumentNo
     * @param instrumentConfigId
     * @param instrumentTypeId
     * @return
     */
    @Override
    public Integer selectCount(String instrumentNo, Integer instrumentConfigId, Integer instrumentTypeId) {

        return  instrumentparamconfigDao.selectCount(Wrappers.lambdaQuery(new InstrumentparamconfigPo())
                .eq(InstrumentparamconfigPo::getInstrumentno,instrumentNo)
                .eq(InstrumentparamconfigPo::getInstrumentconfigid,instrumentConfigId)
                .eq(InstrumentparamconfigPo::getInstrumenttypeid,instrumentTypeId));
    }

    /**
     * 批量删除探头信息
     *
     * @param instrumentParamConfigNos
     */
    @Override
    public void deleteInfos(String[] instrumentParamConfigNos) {
        instrumentparamconfigDao.deleteInfos(instrumentParamConfigNos);
    }

    /**
     * 通过探头no查询探头信息
     *
     * @param instrumentparamconfigno
     * @return
     */
    @Override
    public InstrumentparamconfigDTO selectInstrumentparamconfigInfo(String instrumentparamconfigno) {
        InstrumentparamconfigPo instrumentparamconfigPo = instrumentparamconfigDao.selectById(instrumentparamconfigno);
        return BeanConverter.convert(instrumentparamconfigPo,InstrumentparamconfigDTO.class);
    }

    /**
     * 查询探头参数配置信息
     *
     * @return
     */
    @Override
    public List<InstrumentparamconfigDTO> selectInstrumentparamconfigAllInfo() {
        List<InstrumentparamconfigPo> instrumentparamconfigPos = instrumentparamconfigDao.selectList(null);
        return BeanConverter.convert(instrumentparamconfigPos,InstrumentparamconfigDTO.class);
    }

    /**
     * 更新最新一次的报警时间
     *
     * @param instrumentParamConfigNo 探头检测信息id
     * @param warningTime             报警时间
     */
    @Override
    public void editWarningTime(String instrumentParamConfigNo, Date warningTime) {
        InstrumentparamconfigPo instrumentparamconfigPo = new InstrumentparamconfigPo();
        instrumentparamconfigPo.setInstrumentparamconfigno(instrumentParamConfigNo);
        instrumentparamconfigPo.setWarningtime(warningTime);
        instrumentparamconfigDao.updateById(instrumentparamconfigPo);
    }

    /**
     * @param equipmentNo
     * @return
     */
    @Override
    public List<InstrumentparamconfigDTO> getInstrumentParamConfigInfo(String equipmentNo) {
        return instrumentparamconfigDao.getInstrumentParamConfigInfo(equipmentNo);
    }

    /**
     * @param probeList
     */
    @Override
    public void insertBatch(List<InstrumentparamconfigDTO> probeList) {
        List<InstrumentparamconfigPo> convert = BeanConverter.convert(probeList, InstrumentparamconfigPo.class);
        instrumentparamconfigDao.insertBatchSomeColumn(convert);
    }

    /**
     * @param equipmentNo
     * @return
     */
    @Override
    public List<String> getEquipmentAddProbeInfo(String equipmentNo) {
        return instrumentparamconfigDao.getEquipmentAddProbeInfo(equipmentNo);
    }

    @Override
    public void batchUpdateProbeAlarmState(String warningPhone, String equipmentNo) {
        instrumentparamconfigDao.batchUpdateProbeAlarmState(warningPhone,equipmentNo);
    }

    @Override
    public List<InstrumentparamconfigDTO> getInstrumentParamConfigByCodeAndTypeId(String hospitalCode, String equipmentTypeId) {
        return instrumentparamconfigDao.getInstrumentParamConfigByCodeAndTypeId(hospitalCode,equipmentTypeId);
    }

    @Override
    public void batchProbeAlarmState(List<String> probeIds, String warningPhone) {
        LambdaUpdateWrapper<InstrumentparamconfigPo> instrumentParamConfigPoLambdaUpdateWrapper = Wrappers.lambdaUpdate();
        instrumentParamConfigPoLambdaUpdateWrapper.in(InstrumentparamconfigPo::getInstrumentparamconfigno, probeIds);
        instrumentParamConfigPoLambdaUpdateWrapper.set(InstrumentparamconfigPo::getWarningphone, warningPhone);
        instrumentparamconfigDao.update(new InstrumentparamconfigPo(),instrumentParamConfigPoLambdaUpdateWrapper);
    }

}
