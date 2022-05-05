package com.hc.service.serviceimpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.hc.infrastructure.dao.RepairinfoDao;
import com.hc.po.Monitorinstrument;
import com.hc.po.Repairinfo;
import com.hc.mapper.laboratoryFrom.RepairinfoMapper;
import com.hc.service.RepairinfoService;
import com.hc.service.UpdateRecordService;
import com.hc.units.ApiResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by 15350 on 2020/5/27.
 */
@Service
public class RepairinfoServiceImpl implements RepairinfoService {
    @Autowired
    private RepairinfoDao repairinfoDao;

    @Autowired
    private RepairinfoMapper repairinfoMapper;

    @Autowired
    private UpdateRecordService updateRecordService;

    @Override
    public ApiResponse<List<Monitorinstrument>> getSnByEquipmentNo(String equipmentno) {
        List<Monitorinstrument> monitorInstrumentSn = repairinfoMapper.getMonitorInstrumentSn(equipmentno);
        ApiResponse<List<Monitorinstrument>> apiResponse = new ApiResponse<>();
        if (CollectionUtils.isEmpty(monitorInstrumentSn)){
            apiResponse.setCode(ApiResponse.NOT_FOUND);
            apiResponse.setMessage("无信息");
            return apiResponse;
        }
        apiResponse.setResult(monitorInstrumentSn);
        return apiResponse;
    }

    @Override
    public ApiResponse<Repairinfo> addRepairinfo(Repairinfo repairinfo) {
        //新增数据
        repairinfo.setLogid(UUID.randomUUID().toString().replaceAll("-", ""));
        repairinfo.setRepairdate(new Date());
        Repairinfo save = repairinfoDao.save(repairinfo);
        ApiResponse<Repairinfo> apiResponse = new ApiResponse<>();
        apiResponse.setResult(save);

        return apiResponse;
    }

    @Override
    public ApiResponse<String> updateRepairinfo(Repairinfo repairinfo) {
        Repairinfo repairinfo1 = repairinfoDao.saveAndFlush(repairinfo);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        return apiResponse;
    }

    @Override
    public ApiResponse<String> deleteRepairinfo(Repairinfo repairinfo) {
        repairinfoDao.delete(repairinfo.getLogid());
        ApiResponse<String> apiResponse = new ApiResponse<>();

        return apiResponse;
    }

    @Override
    public ApiResponse<Page<Repairinfo>> selectPageInfo(int pagesize, int pagenum, String beginDate, String endDate, String hospitaname, String equipmenttype, String equipmentname, String repairtype) {
        Integer start = (pagenum - 1) * pagesize;
        Integer end = pagesize;
        PageRowBounds rowBounds = new PageRowBounds(start, end);
        Repairinfo repairinfo = new Repairinfo();
        repairinfo.setBeginDate(beginDate);
        repairinfo.setEndDate(endDate);
        repairinfo.setHospitalname(hospitaname);
        repairinfo.setEquipmentname(equipmentname);
        repairinfo.setEquipmenttype(equipmenttype);
        repairinfo.setRepairtype(repairtype);
        List<Repairinfo> pageInfo = repairinfoMapper.getPageInfo(rowBounds, repairinfo);
        ApiResponse<Page<Repairinfo>> apiResponse = new ApiResponse<>();
        if (CollectionUtils.isEmpty(pageInfo)) {
            apiResponse.setCode(ApiResponse.NOT_FOUND);
            apiResponse.setMessage("无信息");
            return apiResponse;
        }
        PageInfo<Repairinfo> pageInfoa = new PageInfo<Repairinfo>(pageInfo);

        apiResponse.setPage(pageInfoa);
        return apiResponse;
    }
}
