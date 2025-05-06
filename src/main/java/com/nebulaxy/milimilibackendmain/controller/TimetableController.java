package com.nebulaxy.milimilibackendmain.controller;

import com.nebulaxy.milimilibackendmain.common.Result;
import com.nebulaxy.milimilibackendmain.entity.Timetable;
import com.nebulaxy.milimilibackendmain.service.TimetableService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/timetable")
public class TimetableController {

    @Resource
    TimetableService timetableService;

    /**
     *  新增课程表
     */
    @PostMapping("/add")
    public Result addTimetable(@RequestBody Timetable timetable) {
        timetableService.addTimetable(timetable);
        return Result.success();
    }
    
    /**
     * 获取用户课程列表
     */
    @GetMapping("/list")
    public Result getCoursesByUid(@RequestParam Integer uid) {
        try {
            List<Timetable> courses = timetableService.getCoursesByUid(uid);
            return Result.success(courses);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("500", "获取课程数据失败");
        }
    }
    
    /**
     * 更新课程状态
     */
    @PutMapping("/update/status")
    public Result updateCourseStatus(@RequestBody Map<String, Object> params) {
        try {
            Integer classid = Integer.parseInt(params.get("classid").toString());
            Integer status = Integer.parseInt(params.get("status").toString());
            
            timetableService.updateCourseStatus(classid, status);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("500", "更新课程状态失败");
        }
    }
    
    /**
     * 更新单个课程信息
     */
    @PutMapping("/update")
    public Result updateCourse(@RequestBody Timetable timetable) {
        try {
            timetableService.updateTimetable(timetable);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("500", "更新课程信息失败");
        }
    }
    
    /**
     * 批量更新课程信息
     */
    @PostMapping("/batch")
    public Result batchUpdateCourses(@RequestBody List<Timetable> courses) {
        try {
            if (courses == null || courses.isEmpty()) {
                return Result.error("400", "参数格式错误");
            }
            
            timetableService.batchUpdateCourses(courses);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("500", "批量更新课程失败");
        }
    }
    
    /**
     * 删除课程
     */
    @DeleteMapping("/{classid}")
    public Result deleteCourse(@PathVariable Integer classid) {
        try {
            timetableService.deleteCourse(classid);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("500", "删除课程失败");
        }
    }
}
