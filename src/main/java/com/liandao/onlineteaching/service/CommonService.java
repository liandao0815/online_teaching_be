package com.liandao.onlineteaching.service;

import com.github.pagehelper.PageHelper;
import com.liandao.onlineteaching.dao.*;
import com.liandao.onlineteaching.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CommonService {
    private LiveRoomMapper liveRoomMapper;
    private CourseMapper courseMapper;
    private NoteMapper noteMapper;
    private ProblemMapper problemMapper;
    private StatisticsMapper statisticsMapper;

    @Autowired
    public CommonService(LiveRoomMapper liveRoomMapper,
                         CourseMapper courseMapper,
                         NoteMapper noteMapper,
                         ProblemMapper problemMapper,
                         StatisticsMapper statisticsMapper) {
        this.liveRoomMapper = liveRoomMapper;
        this.courseMapper = courseMapper;
        this.noteMapper = noteMapper;
        this.problemMapper = problemMapper;
        this.statisticsMapper = statisticsMapper;
    }

    public Map<String, Object> getSearchData(String value) {
        Map<String, Object> map = new HashMap<>();
        int id = 0;

        if (value.matches("^?[0-9]+$")) {
            id = Integer.parseInt(value);
        }

        Course course = new Course();
        course.setName(value);

        Note note = new Note();
        note.setTitle(value);

        LiveRoom liveRoom = liveRoomMapper.findById(id);

        PageHelper.startPage(1, 3);
        List<Course> courseList = courseMapper.getCourseList(course);

        PageHelper.startPage(1, 3);
        List<Note> noteList = noteMapper.getNoteList(note);

        map.put("liveRoom", liveRoom);
        map.put("course", courseList);
        map.put("note", noteList);

        return map;
    }

    public Map<String, Object> getHomeData() {
        Map<String, Object> map = new HashMap<>();

        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setLiving("1");

        Course course = new Course();
        course.setPriority("1");

        PageHelper.startPage(1, 4);
        List<Course> bannerList = courseMapper.getCourseList(course);

        PageHelper.startPage(1, 4);
        List<LiveRoom> liveRoomList = liveRoomMapper.getLiveRoomList(liveRoom);

        PageHelper.startPage(1, 4);
        List<Course> courseList = courseMapper.getCourseList(null);

        PageHelper.startPage(1, 7);
        List<Note> noteList = noteMapper.getNoteList(null);

        PageHelper.startPage(1, 7);
        List<Problem> problemList = problemMapper.getProblemList();

        map.put("banner", bannerList);
        map.put("liveRoom", liveRoomList);
        map.put("course", courseList);
        map.put("note", noteList);
        map.put("problem", problemList);

        return map;
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    @Transactional
    public void createActivityTask() {
        Calendar now = Calendar.getInstance();

        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int count = statisticsMapper.getActivityByPrevMonth();

        Activity activity = new Activity();
        activity.setYear(year);
        activity.setMonth(month);
        activity.setCount(count);

        statisticsMapper.createActivity(activity);
    }

    public Map<String, Object> getAdminHomeData() {
        List<Integer> todayData = statisticsMapper.getTodayData();
        List<Integer> totalData = statisticsMapper.getTotalData();

        PageHelper.startPage(1,12);
        List<Activity> activityData = statisticsMapper.getActivityData();

        Map<String, Object> map = new HashMap<>();
        map.put("todayData", todayData);
        map.put("totalData", totalData);
        map.put("activityData", activityData);

        return map;
    }
}
