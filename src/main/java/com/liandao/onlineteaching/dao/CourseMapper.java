package com.liandao.onlineteaching.dao;

import com.liandao.onlineteaching.entity.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    void create(Course course);
    List<Course> getCourseList(Course course);
    void delete(int id);
    Course findById(int id);
    void update(Course course);
    List<Course> getCourseListByUser(int userId);
}
