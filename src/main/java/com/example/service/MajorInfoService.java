package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.dao.CollegeInfoDao;
import com.example.dao.MajorInfoDao;
import com.example.entity.CollegeInfo;
import com.example.entity.MajorInfo;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class MajorInfoService {

    @Resource
    private MajorInfoDao majorInfoDao;
    @Resource
    private CollegeInfoDao collegeInfoDao;


    public void add(MajorInfo majorInfo) {
        // verify if the major name already exists
        MajorInfo info = majorInfoDao.findByName(majorInfo.getName());
        if (ObjectUtil.isNotEmpty(info)) {
            throw new CustomException("-1", "您输入的专业名称已存在");
        }
        majorInfoDao.insertSelective(majorInfo);
    }

    public List<MajorInfo> findAll() {
        List<MajorInfo> list = majorInfoDao.selectAll();
        for (MajorInfo majorInfo : list) {
            CollegeInfo collegeInfo = collegeInfoDao.selectByPrimaryKey(majorInfo.getCollegeId());
            majorInfo.setCollegeName(collegeInfo.getName());
        }
        return list;
    }

    public void update(MajorInfo majorInfo) {
        majorInfoDao.updateByPrimaryKeySelective(majorInfo);
    }

    public void deleteById(Long id) {
        majorInfoDao.deleteByPrimaryKey(id);
    }

    public List<MajorInfo> findSearch(String search) {
        // search for major information
        return majorInfoDao.findBySearch(search);
    }
}
