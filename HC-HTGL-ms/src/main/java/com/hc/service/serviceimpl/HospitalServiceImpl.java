package com.hc.service.serviceimpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.hc.config.RedisTemplateUtil;
import com.hc.dao.HospitalofreginfoDao;
import com.hc.entity.Hospitalequiment;
import com.hc.entity.Hospitalofreginfo;
import com.hc.mapper.laboratoryFrom.HospitalInfoMapper;
import com.hc.service.HospitalService;
import com.hc.service.UpdateRecordService;
import com.hc.units.ApiResponse;
import com.hc.units.JsonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by 16956 on 2018-08-05.
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HospitalServiceImpl.class);

    @Autowired
    private HospitalofreginfoDao hospitalofreginfoDao;

    @Autowired
    private HospitalInfoMapper hospitalInfoMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private UpdateRecordService updateRecordService;

    @Override
    public ApiResponse<Hospitalofreginfo> addHosptalInfo(Hospitalofreginfo hospitalofreginfo) {
        ApiResponse<Hospitalofreginfo> apiResponse = new ApiResponse<Hospitalofreginfo>();
        Hospitalofreginfo hospitalofreginfo1 = new Hospitalofreginfo();
        try {
            //验证医院
            hospitalofreginfo1 = hospitalInfoMapper.valideName(hospitalofreginfo);
            if (!StringUtils.isEmpty(hospitalofreginfo1)) {
                apiResponse.setMessage("新增失败，存在当前医院名");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            hospitalofreginfo.setHospitalcode(UUID.randomUUID().toString().replaceAll("-", ""));
            hospitalofreginfo1 = hospitalofreginfoDao.save(hospitalofreginfo);
            String usernames = hospitalofreginfo.getUsernames();
            String hospitalname = hospitalofreginfo.getHospitalname();
            Hospitalofreginfo hospitalofreginfo2 = new Hospitalofreginfo();
            updateRecordService.updateHospotal(hospitalname,usernames,hospitalofreginfo2,hospitalofreginfo1,"0","0");
            apiResponse.setResult(hospitalofreginfo1);
            //添加同步缓存
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            objectObjectObjectHashOperations.put("hospital:info",hospitalofreginfo1.getHospitalcode(),JsonUtil.toJson(hospitalofreginfo1));
            return apiResponse;

        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<Hospitalofreginfo> updateHospital(Hospitalofreginfo hospitalofreginfo) {
        ApiResponse<Hospitalofreginfo> apiResponse = new ApiResponse<Hospitalofreginfo>();
        Hospitalofreginfo hospitalofreginfo1 = new Hospitalofreginfo();
        try {
            hospitalofreginfo1 = hospitalInfoMapper.valideName(hospitalofreginfo);
            if (!ObjectUtils.isEmpty(hospitalofreginfo1)) {
                apiResponse.setMessage("修改失败，存在当前医院名");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            String usernames = hospitalofreginfo.getUsernames();
            String hospitalname = hospitalofreginfo.getHospitalname();
            Hospitalofreginfo one = hospitalofreginfoDao.getOne(hospitalofreginfo.getHospitalcode());
            updateRecordService.updateHospotal(hospitalname,usernames,one,hospitalofreginfo,"0","1");
            hospitalofreginfo1 = hospitalofreginfoDao.save(hospitalofreginfo);

            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            objectObjectObjectHashOperations.delete("hospital:info",hospitalofreginfo.getHospitalcode());
            objectObjectObjectHashOperations.put("hospital:info",hospitalofreginfo1.getHospitalcode(), JsonUtil.toJson(hospitalofreginfo1));
            apiResponse.setResult(hospitalofreginfo1);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<String> deleteHospital(Hospitalofreginfo hospitalofreginfo) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        List<Hospitalequiment> hospitalequiment = new ArrayList<Hospitalequiment>();
        try{
            //查询当前医院下面有没有设备信息
            hospitalequiment = hospitalInfoMapper.selectHospitalInfoByCode(hospitalofreginfo);
            if(CollectionUtils.isNotEmpty(hospitalequiment)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("当前医院存在设备信息，无法删除");
                return apiResponse;
            }
            //执行删除医院操作
            String usernames = hospitalofreginfo.getUsernames();
            String hospitalcode = hospitalofreginfo.getHospitalcode();
            Hospitalofreginfo one1 = hospitalofreginfoDao.getOne(hospitalcode);
            String hospitalname = one1.getHospitalname();
            Hospitalofreginfo hospitalofreginfo2 = new Hospitalofreginfo();
            Hospitalofreginfo one = hospitalofreginfoDao.getOne(hospitalofreginfo.getHospitalcode());
            updateRecordService.updateHospotal(hospitalname,usernames,one,hospitalofreginfo2,"0","2");
            hospitalofreginfoDao.delete(hospitalofreginfo.getHospitalcode());

            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            objectObjectObjectHashOperations.delete("hospital:info",hospitalofreginfo.getHospitalcode());
            return apiResponse;
        }catch(Exception e){
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<Page<Hospitalofreginfo>> getHospitalInfoPage(String fuzzy, Integer pagesize, Integer pagenum) {
        ApiResponse<Page<Hospitalofreginfo>> apiResponse = new ApiResponse<Page<Hospitalofreginfo>>();
        List<Hospitalofreginfo> hospitalofreginfoList = new ArrayList<Hospitalofreginfo>();
        try{
            Integer start = (pagenum-1) * pagesize;
            Integer end = pagesize;
            PageRowBounds page = new PageRowBounds(start,end);
            if (!StringUtils.isEmpty(fuzzy)){
                fuzzy = "%" + fuzzy + "%";
            }
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("fuzzy",fuzzy);
            hospitalofreginfoList = hospitalInfoMapper.selectHospitalInfoPage(page,map);
            if (CollectionUtils.isEmpty(hospitalofreginfoList)) {
                apiResponse.setMessage("不存在当前医院信息");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            PageInfo<Hospitalofreginfo> pageInfo = new PageInfo<Hospitalofreginfo>(hospitalofreginfoList);
            apiResponse.setPage(pageInfo);
            return apiResponse;
        }catch(Exception e){
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<List<Hospitalofreginfo>> getHospitalInfo() {
        ApiResponse<List<Hospitalofreginfo>> apiResponse = new ApiResponse<List<Hospitalofreginfo>>();
        List<Hospitalofreginfo> hospitalofreginfoList = new ArrayList<Hospitalofreginfo>();
        try{
            hospitalofreginfoList = hospitalInfoMapper.selectHospitalInfo();
            if (CollectionUtils.isEmpty(hospitalofreginfoList)){
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("当前无医院信息");
                return apiResponse;
            }
            apiResponse.setResult(hospitalofreginfoList);
            return apiResponse;
        }catch (Exception e){
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }
}

