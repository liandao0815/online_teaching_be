package com.liandao.onlineteaching.controller;

import com.liandao.onlineteaching.entity.Course;
import com.liandao.onlineteaching.param.QueryCourse;
import com.liandao.onlineteaching.service.CourseService;
import com.liandao.onlineteaching.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
public class CourseController {
    private CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/course/token")
    @PreAuthorize("hasAuthority('TEACHER')")
    public Map<String, Object> getUploadToken() {
        String upToken = courseService.getUploadToken();
        return ResponseUtil.success(upToken);
    }

    @PostMapping("/course")
    @PreAuthorize("hasAuthority('TEACHER')")
    public Map<String, Object> createCourse(@RequestParam("poster") MultipartFile poster,
                                            @RequestParam("uid") int userId,
                                            @RequestParam("categoryId") int categoryId,
                                            @RequestParam("name") String name,
                                            @RequestParam("introduction") String introduction,
                                            @RequestParam("link") String link) {
        courseService.createCourse(poster, name, introduction, link, userId, categoryId);
        return ResponseUtil.success(null);
    }

    @GetMapping("/course/list")
    public Map<String, Object> getCourseList(@ModelAttribute QueryCourse queryCourseParams) {
        Map<String, Object> courseListData = courseService.getCourseList(queryCourseParams);
        return ResponseUtil.success(courseListData);
    }

    @GetMapping("/course/{id}")
    public Map<String, Object> getCourse(@PathVariable("id") int id) {
        Course course = courseService.getCourse(id);
        return ResponseUtil.success(course);
    }

    @GetMapping("/course/listByUser")
    public Map<String, Object> getCourseListByUser(@RequestParam("uid") int userId) {
        List<Course> courseList = courseService.getCourseListByUser(userId);
        return ResponseUtil.success(courseList);
    }

    @DeleteMapping("/admin/course/{id}")
    public Map<String, Object> deleteCourse(@PathVariable("id") int id) {
        courseService.deleteCourse(id);
        return ResponseUtil.success(null);
    }

    @PatchMapping("/admin/course/{id}")
    public Map<String, Object> updateCourse(@PathVariable("id") int id, @RequestBody Course course) {
        course.setId(id);
        courseService.updateCourse(course);
        return ResponseUtil.success(null);
    }
}
