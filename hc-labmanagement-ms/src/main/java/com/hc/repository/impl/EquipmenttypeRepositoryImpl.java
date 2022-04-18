package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import com.hc.repository.EquipmenttypeRepository;
import com.hc.infrastructure.dao.EquipmenttypeDao;
import com.hc.po.EquipmenttypePo;


@Repository
public class EquipmenttypeRepositoryImpl extends ServiceImpl<EquipmenttypeDao,EquipmenttypePo> implements EquipmenttypeRepository  {


}