package com.hc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.dto.InstrumentconfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.repository.InstrumentparamconfigRepository;
import com.hc.vo.equimenttype.InstrumentparamconfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class InstrumentparamconfigServiceImpl implements InstrumentparamconfigService {

    @Autowired
    private InstrumentparamconfigRepository instrumentparamconfigRepository;
    /**
     * 插入探头配置信息
     *
     * @param instrumentparamconfigDTO
     */
    @Override
    public void insertInstrumentmonitor(InstrumentparamconfigDTO instrumentparamconfigDTO) {
        instrumentparamconfigRepository.insertInstrumentmonitor(instrumentparamconfigDTO);
    }

    /**
     * 仪器配置参数集合
     *
     * @param instrumentNos
     * @return
     */
    @Override
    public List<InstrumentparamconfigDTO> slectInfo(List<String> instrumentNos) {
        return instrumentparamconfigRepository.slectinfo(instrumentNos);
    }

    /**
     * 删除探头参数信息
     *
     * @param instrumentno
     */
    @Override
    public void deleteInfoByEno(String instrumentno) {
        instrumentparamconfigRepository.deleteInfoByEno(instrumentno);
    }

    @Override
    public List<InstrumentconfigDTO> selectInstrumentparamconfigByEqNo(String equipmentNo) {
        return instrumentparamconfigRepository.selectInstrumentparamconfigByEqNo(equipmentNo);
    }

    /**
     * 更新探头配置信息
     *
     * @param instrumentparamconfigDTO
     */
    @Override
    public void updateInfo(InstrumentparamconfigDTO instrumentparamconfigDTO) {
        instrumentparamconfigRepository.updateInfo(instrumentparamconfigDTO);
    }

    /**
     * 删除探头头信息
     *
     * @param instrumentparamconfigno
     */
    @Override
    public void deleteInfo(InstrumentparamconfigDTO instrumentparamconfigno) {
        instrumentparamconfigRepository.deleteInstrumentparamconfig(instrumentparamconfigno);
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
        return instrumentparamconfigRepository.findInstrumentparamconfig(page,instrumentparamconfigCommand);
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
        return instrumentparamconfigRepository.selectCount(instrumentNo,instrumentConfigId,instrumentTypeId);
    }

    /**
     * 批量删除探头信息
     *
     * @param instrumentParamConfigNos
     */
    @Override
    public void deleteInfos(String[] instrumentParamConfigNos) {
        instrumentparamconfigRepository.deleteInfos(instrumentParamConfigNos);
    }

    /**
     * 通过探头no查询探头信息
     *
     * @param instrumentparamconfigno
     * @return
     */
    @Override
    public InstrumentparamconfigDTO selectInstrumentparamconfigInfo(String instrumentparamconfigno) {
        return instrumentparamconfigRepository.selectInstrumentparamconfigInfo(instrumentparamconfigno);
    }

    /**
     * 查询所有的信息
     *
     * @return
     */
    @Override
    public List<InstrumentparamconfigDTO> selectInstrumentparamconfigAllInfo() {
        return instrumentparamconfigRepository.selectInstrumentparamconfigAllInfo();
    }

    /**
     * 更新最新一次的报警时间
     *  @param instrumentParamConfigNo 探头检测信息id
     * @param warningTime             报警时间
     */
    @Override
    public void editWarningTime(String instrumentParamConfigNo, String warningTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(warningTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        instrumentparamconfigRepository.editWarningTime(instrumentParamConfigNo,date);
    }
}
