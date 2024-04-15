package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.dao.*;
import com.example.entity.*;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
public class RegisterInfoService {

    @Resource
    private RegisterInfoDao registerInfoDao;
    @Resource
    private MajorInfoDao majorInfoDao;
    @Resource
    private TeacherInfoDao teacherInfoDao;
    @Resource
    private StudentInfoDao studentInfoDao;
    @Resource
    private CourseInfoDao courseInfoDao;

    public List<RegisterInfo> findAll(HttpServletRequest request) {
        Account user = (Account) request.getSession().getAttribute("user");
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException("-1", "Please login again");
        }
        List<RegisterInfo> list;
        if (1 == user.getLevel()) {
            // login as admin
            list = registerInfoDao.selectAll();
        } else if (2 == user.getLevel()) {
            // teacher
            list = registerInfoDao.findByCondition(user.getId(), null);
        } else {
            // student
            list = registerInfoDao.findByCondition(null, user.getId());
        }

        for (RegisterInfo registerInfo : list) {
            MajorInfo majorInfo = majorInfoDao.selectByPrimaryKey(registerInfo.getMajorId());
            TeacherInfo teacherInfo = teacherInfoDao.selectByPrimaryKey(registerInfo.getTeacherId());
            StudentInfo studentInfo = studentInfoDao.selectByPrimaryKey(registerInfo.getStudentId());
            registerInfo.setMajorName(majorInfo.getName());
            registerInfo.setTeacherName(teacherInfo.getName());
            registerInfo.setStudentName(studentInfo.getName());
        }
        return list;
    }

    public void add(RegisterInfo registerInfo) {
        registerInfoDao.insertSelective(registerInfo);
    }

    public RegisterInfo find(String name, Long teacherId, Long studentId) {
        return registerInfoDao.find(name, teacherId, studentId);
    }

    public void delete(Long id) {
        RegisterInfo registerInfo = registerInfoDao.selectByPrimaryKey(id);
        CourseInfo courseInfo = courseInfoDao.findByNameAndTeacher(registerInfo.getName(), registerInfo.getTeacherId());
        registerInfoDao.deleteByPrimaryKey(id);
        courseInfo.setEnrolled(courseInfo.getEnrolled() - 1);
        courseInfoDao.updateByPrimaryKeySelective(courseInfo);
    }

    public void update(RegisterInfo registerInfo) {
        registerInfoDao.updateByPrimaryKeySelective(registerInfo);
    }

    /*public PageInfo<RegisterInfo> findPageName(Integer pageNum, Integer pageSize, String name, HttpServletRequest request) {
        Account user = (Account) request.getSession().getAttribute("user");
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException("-1", "登录已失效，请重新登录");
        }
        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        List<RegisterInfo> list = getRegisterInfos(name, user);
        return PageInfo.of(list);
    }

    public PageInfo<RegisterInfo> findPage(Integer pageNum, Integer pageSize, HttpServletRequest request) {
        Account user = (Account) request.getSession().getAttribute("user");
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException("-1", "登录已失效，请重新登录");
        }
        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        List<RegisterInfo> list = getRegisterInfos(null, user);
        return PageInfo.of(list);
    }

     */

}
