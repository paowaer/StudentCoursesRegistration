package com.example.controller;
import com.example.common.Result;
import com.example.entity.CollegeInfo;
import com.example.service.CollegeInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/collegeInfo")
public class CollegeInfoController {

    @Resource
    private CollegeInfoService collegeInfoService;

    @GetMapping
    public Result findAll() {
        List<CollegeInfo> list = collegeInfoService.findAll();
        return Result.success(list);
    }
    @PostMapping
    public Result add(@RequestBody CollegeInfo collegeInfo) {
        collegeInfoService.add(collegeInfo);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody CollegeInfo collegeInfo) {
        collegeInfoService.update(collegeInfo);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        collegeInfoService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{search}")
    public Result find(@PathVariable String search) {
        List<CollegeInfo> list = collegeInfoService.find(search);
        return Result.success(list);
    }

}
