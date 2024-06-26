package com.example.controller;

import com.example.common.Result;
import com.example.entity.MajorInfo;
import com.example.service.MajorInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/majorInfo")
public class MajorInfoController {

    @Resource
    private MajorInfoService majorInfoService;

    @PostMapping
    public Result add(@RequestBody MajorInfo majorInfo) {
        majorInfoService.add(majorInfo);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody MajorInfo majorInfo) {
        majorInfoService.update(majorInfo);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        List<MajorInfo> list = majorInfoService.findAll();
        return Result.success(list);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        majorInfoService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{search}")
    public Result findSearch(@PathVariable String search) {
        List<MajorInfo> list = majorInfoService.findSearch(search);
        return Result.success(list);
    }

}
