package com.example.service;

import com.example.dao.CourseInfoDao;

import com.example.entity.CourseInfo;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class CourseInfoService {

    @Resource
    private CourseInfoDao courseInfoDao;

    public void add(CourseInfo courseInfo) {
        courseInfoDao.insertSelective(courseInfo);
    }

    public List<CourseInfo> findAll() {
        return courseInfoDao.findAll();
    }

    public void update(CourseInfo courseInfo) {
        courseInfoDao.updateByPrimaryKeySelective(courseInfo);
    }

    public void delete(Long id) {
        courseInfoDao.deleteByPrimaryKey(id);
    }

    public List<CourseInfo> findSearch(String search) {
        return courseInfoDao.findSearch(search);
    }
}
