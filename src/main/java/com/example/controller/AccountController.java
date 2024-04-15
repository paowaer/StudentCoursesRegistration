package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.common.Result;
import com.example.entity.AdminInfo;
import com.example.entity.StudentInfo;
import com.example.entity.TeacherInfo;
import com.example.service.AdminInfoService;
import com.example.service.StudentInfoService;
import com.example.service.TeacherInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RequestMapping
@RestController
public class AccountController {

    /**
     * description：login
     * @param user
     * @param request
     * @return Result
     */
    @Resource
    private AdminInfoService adminInfoService;
    @Resource
    private TeacherInfoService teacherInfoService;
    @Resource
    private StudentInfoService studentInfoService;

    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
         //png类型
        /*SpecCaptcha captcha = new SpecCaptcha(135, 33, 6);
        captcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);
        CaptchaUtil.out(captcha, request, response);
         */

        // 算术类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(135, 33);
        captcha.setLen(4);  // 几位数运算，默认是两位
        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
        captcha.text();  // 获取运算的结果：5
        CaptchaUtil.out(captcha, request, response);


    }


    @PostMapping("/login")
    public Result login(@RequestBody Account user, HttpServletRequest request) {

        // verify if the data is empty
        if (ObjectUtil.isEmpty(user.getName()) || ObjectUtil.isEmpty(user.getPassword()) || ObjectUtil.isEmpty(user.getLevel())) {
            return Result.error("-1", "Invalid data, please check and try again.");
        }

        if (!CaptchaUtil.ver(user.getVerCode(), request)) {
            // clear the verification code in session
            CaptchaUtil.clear(request);
            return Result.error("1001", "the verification code is incorrect.");
        }

        Integer level = user.getLevel();
        Account loginUser = new Account();
        if (level == 1) {
            // login as an admin
            loginUser = adminInfoService.login(user.getName(), user.getPassword());
        }
        if (level == 2) {
            // login as a teacher
            loginUser = teacherInfoService.login(user.getName(), user.getPassword());
        }
        if (level == 3) {
            // login as a student
            loginUser = studentInfoService.login(user.getName(), user.getPassword());
        }

        // save user info in session
        request.getSession().setAttribute("user", loginUser);

        return Result.success(loginUser);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Account user, HttpServletRequest request) {
        // verify if the data is empty
        if (ObjectUtil.isEmpty(user.getName()) || ObjectUtil.isEmpty(user.getPassword()) || ObjectUtil.isEmpty(user.getLevel())) {
            return Result.error("-1", "Invalid data, please check and try again.");
        }

        Integer level = user.getLevel();
        if (2 == level) {
            // register as a teacher
            TeacherInfo teacherInfo = new TeacherInfo();
            BeanUtils.copyProperties(user, teacherInfo);
            teacherInfoService.add(teacherInfo);
        }
        if (3 == level) {
            // register as a student
            StudentInfo studentInfo = new StudentInfo();
            BeanUtils.copyProperties(user, studentInfo);
            studentInfoService.register(studentInfo);
        }
        return Result.success();
    }

    @GetMapping("/getUser")
    public Result getUser(HttpServletRequest request) {
        // get the current user from session
        Account user = (Account)request.getSession().getAttribute("user");
        // verify what level the user is
        Integer level = user.getLevel();

        if (level == 1) {
            // login as an admin
            AdminInfo adminInfo = adminInfoService.findById(user.getId());
            return Result.success(adminInfo);
        }
        if (level == 2) {
            // login as a teacher
            TeacherInfo teacherInfo = teacherInfoService.findById(user.getId());
            return Result.success(teacherInfo);
        }
        if (level == 3) {
            // login as a student
            StudentInfo studentInfo = studentInfoService.findById(user.getId());
            return Result.success(studentInfo);
        }
        return Result.success(new Account());
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account, HttpServletRequest request) {
        // 1. verify which user is currently logged in
        Account user = (Account) request.getSession().getAttribute("user");
        // 2. verify the original password
        String oldPassword = account.getPassword();
        if (!user.getPassword().equals(oldPassword)) {
            return Result.error("-1", "the original password is incorrect.");
        }
        String newPassword = account.getNewPassword();
        // update the password according to the user level
        Integer level = user.getLevel();
        if (1 == level) {
            AdminInfo adminInfo = new AdminInfo();
            BeanUtils.copyProperties(user, adminInfo);
            adminInfo.setPassword(newPassword);
            adminInfoService.update(adminInfo);
        }
        if (2 == level) {
            TeacherInfo teacherInfo = new TeacherInfo();
            BeanUtils.copyProperties(user, teacherInfo);
            teacherInfo.setPassword(newPassword);
            teacherInfoService.update(teacherInfo);
        }
        if (3 == level) {
            StudentInfo studentInfo = new StudentInfo();
            BeanUtils.copyProperties(user, studentInfo);
            studentInfo.setPassword(newPassword);
            studentInfoService.update(studentInfo);
        }
        // 3. clear the original session
        request.getSession().setAttribute("user", null);
        return Result.success();
    }

    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return Result.success();
    }

}
