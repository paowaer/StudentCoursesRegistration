package com.example.dao;

import com.example.entity.MajorInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface MajorInfoDao extends Mapper<MajorInfo> {

    @Select("select * from major_info where name = #{name}")
    MajorInfo findByName(@Param("name") String name);

    @Select("select a.*, b.name AS collegeName from major_info AS a LEFT JOIN college_info AS b ON a.collegeId = b.id where a.name like concat('%', #{search}, '%')")
    List<MajorInfo> findBySearch(@Param("search") String search);

}
