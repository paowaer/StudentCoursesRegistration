package com.example.controller;

import com.example.common.Result;
import com.example.entity.RegisterInfo;
import com.example.service.RegisterInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/registerInfo")
public class RegisterInfoController {

    @Resource
    private RegisterInfoService registerInfoService;

    @GetMapping
    public Result findAll(HttpServletRequest request) {
        List<RegisterInfo> list = registerInfoService.findAll(request);
        return Result.success(list);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        registerInfoService.delete(id);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody RegisterInfo registerInfo) {
        registerInfoService.update(registerInfo);
        return Result.success();
    }

    /*@GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, HttpServletRequest request) {
        PageInfo<RegisterInfo> info = registerInfoService.findPage(pageNum, pageSize, request);
        return Result.success(info);
    }

    @GetMapping("/page/{name}")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @PathVariable String name, HttpServletRequest request) {
        PageInfo<RegisterInfo> info = registerInfoService.findPageName(pageNum, pageSize, name, request);
        return Result.success(info);
    }

     */
}
