package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.ResultCode;
import com.example.dao.TeacherInfoDao;
import com.example.entity.Account;
import com.example.entity.TeacherInfo;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TeacherInfoService {

    @Resource
    private TeacherInfoDao teacherInfoDao;

    public Account login(String name, String password) {
        // access the database to check if the account exists
        // if the account exists, return the account information
        TeacherInfo teacherInfo = teacherInfoDao.findByNameAndPassword(name, password);

        // if the account does not exist, throw an exception
        if (ObjectUtil.isEmpty(teacherInfo)) {
            throw new CustomException("-1", "Invalid username or password");
        }
        return teacherInfo;
    }

    public TeacherInfo findById(Long id) {
        return teacherInfoDao.selectByPrimaryKey(id);
    }

    public void update(TeacherInfo teacherInfo) {
        teacherInfoDao.updateByPrimaryKeySelective(teacherInfo);
    }

    public List<TeacherInfo> findAll() {
        return teacherInfoDao.selectAll();
    }

    public void add(TeacherInfo teacherInfo) {
        //1. Check if the user already exists
        TeacherInfo info = teacherInfoDao.findByName(teacherInfo.getName());
        //2. If the user exist, return an error message
        if (ObjectUtil.isNotEmpty(info)) {
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(teacherInfo.getPassword())) {
            teacherInfo.setPassword("123456");
        //3. If the user does not exist, insert the user into the database
            teacherInfo.setLevel(2);
        teacherInfoDao.insertSelective(teacherInfo);
        }
    }

    public void deleteById(Long id) {
        teacherInfoDao.deleteByPrimaryKey(id);
    }

    public PageInfo<TeacherInfo> findPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TeacherInfo> list = teacherInfoDao.selectAll();
        return PageInfo.of(list);
    }

    public PageInfo<TeacherInfo> findPageSearch(String search, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TeacherInfo> teacherInfos = teacherInfoDao.findByLikeName(search);
        return PageInfo.of(teacherInfos);
    }
}
