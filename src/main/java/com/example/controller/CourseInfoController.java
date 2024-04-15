package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.CourseInfo;
import com.example.entity.RegisterInfo;
import com.example.exception.CustomException;
import com.example.service.CourseInfoService;
import com.example.service.RegisterInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/courseInfo")
public class CourseInfoController {

    @Resource
    private CourseInfoService courseInfoService;
    @Resource
    private RegisterInfoService registerInfoService;

    @PostMapping
    public Result add(@RequestBody CourseInfo courseInfo) {
        courseInfoService.add(courseInfo);
        return Result.success();
    }

    @PostMapping("/register")
    public Result register(@RequestBody CourseInfo courseInfo, HttpServletRequest request) {

        Account user = (Account) request.getSession().getAttribute("user");
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException("-1", "Please re-login");
        }

        // 1. very if the user has already registered this course
        RegisterInfo info = registerInfoService.find(courseInfo.getName(), courseInfo.getTeacherId(), user.getId());
        if (ObjectUtil.isNotEmpty(info)) {
            throw new CustomException("-1", "You have already registered this course.");
        }

        // 2. copy the course info to register info
        RegisterInfo registerInfo = new RegisterInfo();
        BeanUtils.copyProperties(courseInfo, registerInfo);
        registerInfo.setId(null);

        // 3. fill in the student id and status
        registerInfo.setStudentId(user.getId());
        registerInfo.setStatus("Awaiting");

        registerInfoService.add(registerInfo);

        // 3. add the number of students enrolled in the course
        courseInfo.setEnrolled(courseInfo.getEnrolled() + 1);
        courseInfoService.update(courseInfo);

        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody CourseInfo courseInfo) {
        courseInfoService.update(courseInfo);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        courseInfoService.delete(id);
        return Result.success();
    }

    @GetMapping("/{search}")
    public Result findSearch(@PathVariable String search) {
        List<CourseInfo> list = courseInfoService.findSearch(search);
        return Result.success(list);
    }

    @GetMapping
    public Result findAll() {
        List<CourseInfo> list = courseInfoService.findAll();
        return Result.success(list);
    }

}
