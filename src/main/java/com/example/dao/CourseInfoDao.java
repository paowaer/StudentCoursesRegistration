package com.example.dao;

import com.example.entity.CourseInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CourseInfoDao extends Mapper<CourseInfo> {

    @Select("select a.*, b.name AS teacherName from course_info AS a LEFT JOIN teacher_info AS b ON a.teacherId = b.id where a.name like concat('%', #{search}, '%')")
    List<CourseInfo> findSearch(@Param("search") String search);

    @Select("select a.*, b.name AS teacherName from course_info AS a LEFT JOIN teacher_info AS b ON a.teacherId = b.id")
    List<CourseInfo> findAll();

    @Select("select * from course_info where name = #{name} and teacherId = #{teacherId} limit 1")
    CourseInfo findByNameAndTeacher(@Param("name") String name, @Param("teacherId") Long teacherId);

}
