package com.example.dao;

import com.example.entity.RegisterInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Repository
public interface RegisterInfoDao extends Mapper<RegisterInfo> {

    @Select("select * from register_info where name = #{name} and teacherId = #{teacherId} and studentId = #{studentId} limit 1")
    RegisterInfo find(@Param("name") String name, @Param("teacherId") Long teacherId, @Param("studentId") Long studentId);

    List<RegisterInfo> findByCondition(@Param("teacherId") Long teacherId, @Param("studentId") Long studentId);

}
