package com.nebulaxy.milimilibackendmain.service;

import com.nebulaxy.milimilibackendmain.entity.Timetable;
import com.nebulaxy.milimilibackendmain.mapper.TimetableMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TimetableService {

    @Resource
    TimetableMapper timetableMapper;

    /**
     * 添加课程
     */
    public void addTimetable(Timetable timetable) {
        // 检查是否已存在相同课程
        Timetable dbInfo = timetableMapper.selectTimetable(
                timetable.getExam_table(),
                timetable.getCourse_name(),
                timetable.getTeacher_name(),
                timetable.getUid()
        );
        
        if (dbInfo != null) {
            // 如果存在，则更新
            timetableMapper.updateTimetable(timetable);
        } else {
            // 如果不存在，则插入
            timetableMapper.insertTimetable(timetable);
        }
    }

    /**
     * 获取用户课程列表
     */
    public List<Timetable> getCoursesByUid(Integer uid) {
        return timetableMapper.selectByUid(uid);
    }

    /**
     * 更新课程状态
     */
    public void updateCourseStatus(Integer classid, Integer status) {
        timetableMapper.updateStatus(classid, status);
    }
    
    /**
     * 更新课程信息
     */
    public void updateTimetable(Timetable timetable) {
        timetableMapper.updateTimetableById(timetable);
    }

    /**
     * 批量更新课程信息
     */
    @Transactional
    public void batchUpdateCourses(List<Timetable> courses) {
        if (courses == null || courses.isEmpty()) {
            return;
        }
        
        // 获取第一个课程的用户ID
        Integer uid = courses.get(0).getUid();
        
        // 先删除该用户的所有课程
        timetableMapper.deleteByUid(uid);
        
        // 然后重新插入所有课程
        for (Timetable course : courses) {
            timetableMapper.insertTimetable(course);
        }
    }
    
    /**
     * 删除课程
     */
    public void deleteCourse(Integer classid) {
        timetableMapper.deleteById(classid);
    }
}
