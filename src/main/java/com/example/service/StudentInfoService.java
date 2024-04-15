package com.example.service;


import cn.hutool.core.util.ObjectUtil;
import com.example.common.ResultCode;
import com.example.dao.CollegeInfoDao;
import com.example.dao.StudentInfoDao;
import com.example.entity.Account;
import com.example.entity.CollegeInfo;
import com.example.entity.StudentInfo;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentInfoService {

    @Resource
    private StudentInfoDao studentInfoDao;
    @Resource
    private CollegeInfoDao collegeInfoDao;

    public Account login(String name, String password) {
        // 1. check if the account exists
        StudentInfo studentInfo = studentInfoDao.findByNameAndPassword(name, password);
        if (ObjectUtil.isEmpty(studentInfo)) {
            // 2. if the account does not exist, throw an exception
            throw new CustomException("-1", "Invalid username or password, please check and try again.");
        }
        return studentInfo;
    }

    public void register(StudentInfo studentInfo) {
        // 1. check if the account exists
        StudentInfo info =  studentInfoDao.findByName(studentInfo.getName());
        // 2. if the account does not exist, insert the account into the database
        studentInfoDao.insertSelective(studentInfo);
    }

    public StudentInfo findById(Long id) {
        return studentInfoDao.selectByPrimaryKey(id);
    }

    public void update(StudentInfo studentInfo) {
        studentInfoDao.updateByPrimaryKeySelective(studentInfo);
    }

    public List<StudentInfo> findAll() {
        List<StudentInfo> list = studentInfoDao.selectAll();
        for (StudentInfo studentInfo : list) {
            // check the college information
            if (ObjectUtil.isNotEmpty(studentInfo.getCollegeId())) {
                CollegeInfo collegeInfo = collegeInfoDao.selectByPrimaryKey(studentInfo.getCollegeId());
                studentInfo.setCollegeName(collegeInfo.getName());
            }
        }
        return list;
        /*return studentInfoDao.findAllJoinCollege();*/
    }

    public void add(StudentInfo studentInfo) {
        //1. Check if the user already exists
        StudentInfo info = studentInfoDao.findByName(studentInfo.getName());
        //2. If the user exist, return an error message
        if (ObjectUtil.isNotEmpty(info)) {
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(studentInfo.getPassword())) {
            studentInfo.setPassword("123456");
            //3. If the user does not exist, insert the user into the database
            studentInfo.setLevel(3);
            studentInfoDao.insertSelective(studentInfo);
        }
    }

    public void deleteById(Long id) {
        studentInfoDao.deleteByPrimaryKey(id);
    }

    public PageInfo<StudentInfo> findPageName(Integer pageNum, Integer pageSize, String name) {
        // start page
        PageHelper.startPage(pageNum, pageSize);
        List<StudentInfo> infos = studentInfoDao.findByNamePage(name);
        for (StudentInfo info : infos) {
            CollegeInfo collegeInfo = collegeInfoDao.selectByPrimaryKey(info.getCollegeId());
            if (ObjectUtil.isNotEmpty(collegeInfo)) {
                info.setCollegeName(collegeInfo.getName());
            }
        }
        return PageInfo.of(infos);
    }

    public PageInfo<StudentInfo> findPage(Integer pageNum, Integer pageSize) {
        // start page
        PageHelper.startPage(pageNum, pageSize);
        // check the database to get all the account information
        List<StudentInfo> infos = studentInfoDao.selectAll();
        for (StudentInfo info : infos) {
            CollegeInfo collegeInfo = collegeInfoDao.selectByPrimaryKey(info.getCollegeId());
            if (ObjectUtil.isNotEmpty(collegeInfo)) {
                info.setCollegeName(collegeInfo.getName());
            }
        }
        return PageInfo.of(infos);
    }
}
