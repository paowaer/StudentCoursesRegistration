package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.ResultCode;
import com.example.dao.AdminInfoDao;
import com.example.entity.Account;
import com.example.entity.AdminInfo;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminInfoService {

    @Resource
    private AdminInfoDao adminInfoDao;

    public Account login(String name, String password) {
        // access the database to check if the account exists
        // if the account exists, return the account information
        AdminInfo adminInfo = adminInfoDao.findByNameAndPassword(name, password);

        // if the account does not exist, throw an exception
        if (ObjectUtil.isEmpty(adminInfo)) {
            throw new CustomException("-1", "Invalid username or password");
        }

        return adminInfo;

    }

    public AdminInfo findById(Long id) {
        return adminInfoDao.selectByPrimaryKey(id);
    }

    public void update(AdminInfo adminInfo) {
        adminInfoDao.updateByPrimaryKeySelective(adminInfo);
    }

    public void add(AdminInfo adminInfo) {

        // 1. check if the account already exists
        AdminInfo info = adminInfoDao.findByName(adminInfo.getName());
        if (ObjectUtil.isNotEmpty(info)) {
            // if the account already exists, throw an exception
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(adminInfo.getPassword())) {
            // if the password is empty, set the password to 123456
            adminInfo.setPassword("123456");
        }
        adminInfoDao.insertSelective(adminInfo);
    }

    public List<AdminInfo> findAll() {
        return adminInfoDao.selectAll();
    }

    public void deleteById(Long id) {
        adminInfoDao.deleteByPrimaryKey(id);
    }

    public PageInfo<AdminInfo> findPage(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        // access the database to get all the account information
        List<AdminInfo> infos = adminInfoDao.selectAll();

        return PageInfo.of(infos);
    }

    public PageInfo<AdminInfo> findPageName(Integer pageNum, Integer pageSize, String name) {
        PageHelper.startPage(pageNum, pageSize);
        List<AdminInfo> infos = adminInfoDao.findByNamePage(name);
        return PageInfo.of(infos);
    }
}
