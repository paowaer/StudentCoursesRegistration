package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.dao.CollegeInfoDao;
import com.example.entity.CollegeInfo;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class CollegeInfoService {

    @Resource
    private CollegeInfoDao collegeInfoDao;

    public void add(CollegeInfo collegeInfo) {
        CollegeInfo info = collegeInfoDao.findByName(collegeInfo.getName());
        if (ObjectUtil.isNotEmpty(info)) {
            throw new CustomException("-1", "this college already exists");
        }
        collegeInfoDao.insertSelective(collegeInfo);
    }

    public List<CollegeInfo> findAll() {
        return collegeInfoDao.selectAll();
    }

    public void update(CollegeInfo collegeInfo) {
        collegeInfoDao.updateByPrimaryKeySelective(collegeInfo);
    }

    public void deleteById(Long id) {
        collegeInfoDao.deleteByPrimaryKey(id);
    }

    public List<CollegeInfo> find(String search) {
        return collegeInfoDao.find(search);
    }
}
