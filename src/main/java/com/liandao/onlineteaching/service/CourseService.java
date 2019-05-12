package com.liandao.onlineteaching.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liandao.onlineteaching.dao.CourseMapper;
import com.liandao.onlineteaching.dao.MessageMapper;
import com.liandao.onlineteaching.entity.Course;
import com.liandao.onlineteaching.entity.Message;
import com.liandao.onlineteaching.param.QueryCourse;
import com.liandao.onlineteaching.utils.QiniuyunUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseService {
    private CourseMapper courseMapper;
    private QiniuyunUtil qiniuyunUtil;
    private MessageMapper messageMapper;

    @Autowired
    public CourseService(CourseMapper courseMapper, QiniuyunUtil qiniuyunUtil, MessageMapper messageMapper) {
        this.courseMapper = courseMapper;
        this.qiniuyunUtil = qiniuyunUtil;
        this.messageMapper = messageMapper;
    }

    public String getUploadToken() {
        return this.qiniuyunUtil.getUploadToken();
    }

    @Transactional
    public void createCourse(MultipartFile poster, String name, String introduction, String link,
                             int userId, int categoryId) {
        String posterScr = qiniuyunUtil.uploadImage(poster);

        Course course = new Course();

        course.setName(name);
        course.setIntroduction(introduction);
        course.setLink(link);
        course.setUserId(userId);
        course.setCategoryId(categoryId);
        course.setPoster(posterScr);
        courseMapper.create(course);
    }

    public Map<String, Object> getCourseList(QueryCourse queryCourseParams) {
        Course course = new Course();
        course.setId(queryCourseParams.getId());
        course.setName(queryCourseParams.getName());
        course.setCategoryId(queryCourseParams.getCategoryId());
        course.setPriority(queryCourseParams.getPriority());

        PageHelper.startPage(queryCourseParams.getPageNum(), queryCourseParams.getPageSize());
        List<Course> courseList = courseMapper.getCourseList(course);

        PageInfo<Course> page = new PageInfo<>(courseList);

        Map<String, Object> map = new HashMap<>();
        map.put("list", courseList);
        map.put("total", page.getTotal());

        return map;
    }

    public Course getCourse(int id) {
        return courseMapper.findById(id);
    }

    public List<Course> getCourseListByUser(int userId) {
        return courseMapper.getCourseListByUser(userId);
    }

    @Transactional
    public void deleteCourse(int id) {
        Course course = courseMapper.findById(id);
        int userId = course.getUserId();
        String name = course.getName();

        Message message = new Message();
        message.setUserId(userId);
        message.setContent("很遗憾，由于您发布的课程【" + name + "】存在不可描述的信息，所以管理员已经将其删除");
        messageMapper.create(message);

        courseMapper.delete(id);
    }

    @Transactional
    public void updateCourse(Course course) {
        courseMapper.update(course);
    }
}
