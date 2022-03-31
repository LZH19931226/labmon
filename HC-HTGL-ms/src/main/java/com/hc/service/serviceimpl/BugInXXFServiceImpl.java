package com.hc.service.serviceimpl;

import com.hc.mapper.InstrumentParamConfigDao;
import com.hc.mapper.MonitorInstrumentDao;
import com.hc.entity.Instrumentmonitor;
import com.hc.entity.Instrumentparamconfig;
import com.hc.entity.Monitorinstrument;
import com.hc.mapper.laboratoryFrom.MonitorInstrumentMapper;
import com.hc.service.BugInXXFService;
import com.hc.units.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by xxf on 2019-02-20.
 */
@Service
public class BugInXXFServiceImpl implements BugInXXFService {
    @Autowired
    private InstrumentParamConfigDao instrumentParamConfigDao;
    @Autowired
    private MonitorInstrumentDao monitorInstrumentDao;
    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;
    @Override
    public ApiResponse<String> update() {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        //获取所有医院设备
        List<Monitorinstrument> all = monitorInstrumentDao.findAll();
        for (Monitorinstrument monitorinstrument : all) {
            Integer instrumenttypeid = monitorinstrument.getInstrumenttypeid();
            if (instrumenttypeid == null || instrumenttypeid == 11) {
                continue;
            }
            String instrumentno = monitorinstrument.getInstrumentno();
            String instrumentname = monitorinstrument.getInstrumentname();
            // 根据instrumenttypeid 获取监控范围值
            List<Instrumentmonitor> instrumentmonitorByTypeid = monitorInstrumentMapper.getInstrumentmonitorByTypeid(instrumenttypeid);
            for (Instrumentmonitor instrumentmonitor : instrumentmonitorByTypeid) {
                BigDecimal highlimit = instrumentmonitor.getHighlimit();
                BigDecimal lowlimit = instrumentmonitor.getLowlimit();
                Integer instrumentconfigid = instrumentmonitor.getInstrumentconfigid();
                Integer instrumenttypeid1 = instrumentmonitor.getInstrumenttypeid();
                Instrumentparamconfig instrumentparamconfig =  new Instrumentparamconfig();
                instrumentparamconfig.setAlarmtime(3);
                instrumentparamconfig.setHighlimit(highlimit);
                instrumentparamconfig.setLowlimit(lowlimit);
                instrumentparamconfig.setInstrumentconfigid(instrumentconfigid);
                instrumentparamconfig.setInstrumenttypeid(instrumenttypeid1);
                instrumentparamconfig.setInstrumentno(instrumentno);
                instrumentparamconfig.setInstrumentname(instrumentname);
                instrumentparamconfig.setWarningphone("0");
                instrumentparamconfig.setInstrumentparamconfigno(UUID.randomUUID().toString().replaceAll("-", ""));
                instrumentParamConfigDao.saveAndFlush(instrumentparamconfig);


            }

        }
        return apiResponse;
    }

    @Override
    public ApiResponse<String> update1() {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        List<Map<String, Object>> xxx = monitorInstrumentMapper.xxx();
        for (Map<String,Object> map : xxx) {
            String sn = map.get("sn").toString();
            Integer instrumentconfigid = new Integer(map.get("instrumentconfigid").toString());
            BigDecimal highlimit = new BigDecimal(map.get("highlimit").toString());
            BigDecimal lowlimit = new BigDecimal(map.get("lowlimit").toString());
            //根据sn查询 当前设备探头
            Monitorinstrument monitorInstrument = monitorInstrumentMapper.getMonitorInstrument(sn);
            if (monitorInstrument != null) {
                String instrumentno = monitorInstrument.getInstrumentno();
                String instrumentname = monitorInstrument.getInstrumentname();
                Instrumentparamconfig instrumentparamconfig = new Instrumentparamconfig();
                instrumentparamconfig.setAlarmtime(3);
                instrumentparamconfig.setHighlimit(highlimit);
                instrumentparamconfig.setLowlimit(lowlimit);
                instrumentparamconfig.setInstrumentconfigid(instrumentconfigid);
                instrumentparamconfig.setInstrumenttypeid(11);
                instrumentparamconfig.setInstrumentno(instrumentno);
                instrumentparamconfig.setInstrumentname(instrumentname);
                instrumentparamconfig.setWarningphone("0");
                instrumentparamconfig.setInstrumentparamconfigno(UUID.randomUUID().toString().replaceAll("-", ""));
                instrumentParamConfigDao.saveAndFlush(instrumentparamconfig);
            }

        }
        return apiResponse;

    }
}
