package com.hc.application;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentMonitorCommand;
import com.hc.dto.InstrumentConfigDTO;
import com.hc.dto.InstrumentmonitorDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.service.InstrumentConfigService;
import com.hc.service.InstrumentmonitorService;
import com.hc.service.MonitorinstrumenttypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Component
public class InstrumentMonitorApplication {

    @Autowired
    private InstrumentmonitorService instrumentmonitorService;

    @Autowired
    private MonitorinstrumenttypeService monitorinstrumenttypeService;

    @Autowired
    private InstrumentConfigService instrumentConfigService;

    /**
     * 添加探头高低值
     * @param instrumentMonitorCommand
     */
    public void add(InstrumentMonitorCommand instrumentMonitorCommand) {
        Integer instrumentTypeId = instrumentMonitorCommand.getInstrumentTypeId();
        List<InstrumentmonitorDTO> list = instrumentmonitorService.selectMonitorEquipmentList(instrumentTypeId);
        if(CollectionUtils.isNotEmpty(list)){
            instrumentmonitorService.removeByTypeId(instrumentTypeId);
        }
        instrumentmonitorService.add(instrumentMonitorCommand);
    }

    /**
     * 分页查询
     * @param instrumentMonitorCommand
     * @return
     */
    public Page<InstrumentmonitorDTO> list(InstrumentMonitorCommand instrumentMonitorCommand) {
        Integer pageCurrent = instrumentMonitorCommand.getPageCurrent();
        Integer pageSize = instrumentMonitorCommand.getPageSize();
        Integer instrumentTypeId = instrumentMonitorCommand.getInstrumentTypeId();
        Integer instrumentConfigId = instrumentMonitorCommand.getInstrumentConfigId();
        Page<InstrumentmonitorDTO> page = new Page<>(pageCurrent,pageSize);
        List<InstrumentmonitorDTO> list = instrumentmonitorService.list(page,instrumentTypeId,instrumentConfigId);
        if(CollectionUtils.isNotEmpty(list)){
            //查询设备字典
            List<MonitorinstrumenttypeDTO> monitorInstrumentTypeList = monitorinstrumenttypeService.selectAll();
            Map<Integer, List<MonitorinstrumenttypeDTO>> typeIdMap
                    = monitorInstrumentTypeList.stream().collect(Collectors.groupingBy(MonitorinstrumenttypeDTO::getInstrumenttypeid));
            //查询监测类型字典
            List<InstrumentConfigDTO>  instrumentConfigList = instrumentConfigService.list();
            Map<Integer, List<InstrumentConfigDTO>> configIdMap
                    = instrumentConfigList.stream().collect(Collectors.groupingBy(InstrumentConfigDTO::getInstrumentconfigid));
            list.forEach(res->{
                if(typeIdMap.containsKey(res.getInstrumenttypeid())){
                    res.setInstrumenttypename(typeIdMap.get(res.getInstrumenttypeid()).get(0).getInstrumenttypename());
                }
                if(configIdMap.containsKey(res.getInstrumentconfigid())){
                    res.setInstrumentconfigname(configIdMap.get(res.getInstrumentconfigid()).get(0).getInstrumentconfigname());
                }
                if(StringUtils.isEmpty(res.getUnit())){
                    res.setUnit("");
                }
                if(StringUtils.isEmpty(res.getStyleMin())){
                    res.setStyleMin("");
                }
                if(StringUtils.isEmpty(res.getStyleMax())){
                    res.setStyleMax("");
                }
            });
            page.setRecords(list);
        }
        return page;
    }

    /**
     * 修改
     * @param instrumentMonitorCommand
     */
    public void edit(InstrumentMonitorCommand instrumentMonitorCommand) {
        InstrumentmonitorDTO instrumentmonitorDTO= buildInstrumentmonitorDTO(instrumentMonitorCommand);
        instrumentmonitorService.updateInstrumentmonitor(instrumentmonitorDTO);
    }

    private InstrumentmonitorDTO buildInstrumentmonitorDTO(InstrumentMonitorCommand res) {
        InstrumentmonitorDTO obj = new InstrumentmonitorDTO();
        obj.setInstrumentconfigid(res.getInstrumentConfigId());
        obj.setInstrumenttypeid(res.getInstrumentTypeId());
        obj.setSaturation(res.getSaturation());
        obj.setLowlimit(res.getLowlimit());
        obj.setHighlimit(res.getHighlimit());
        obj.setChannel(res.getChannel());
        obj.setUnit(res.getUnit());
        obj.setStyleMax(res.getStyleMax());
        obj.setStyleMin(res.getStyleMin());
        return obj;
    }

    /**
     * 删除高低值字典
     * @param instrumentTypeId
     */
    public void remove(Integer instrumentTypeId,Integer instrumentConfigId) {
        instrumentmonitorService.remove(instrumentTypeId,instrumentConfigId);
    }
}
