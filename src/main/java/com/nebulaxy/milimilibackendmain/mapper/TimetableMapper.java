package com.nebulaxy.milimilibackendmain.mapper;

import com.nebulaxy.milimilibackendmain.entity.Timetable;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface TimetableMapper {

    /**
     * 查询课程是否存在
     */
    @Select("SELECT * FROM timetable WHERE exam_table = #{exam_table} AND course_name = #{course_name} AND teacher_name = #{teacher_name} AND uid = #{uid}")
    Timetable selectTimetable(Date exam_table, String course_name, String teacher_name, Integer uid);

    /**
     * 插入课程
     */
    @Insert("INSERT INTO timetable(uid, course_name, exam_table, teacher_name, status) VALUES(#{uid}, #{courseName}, #{examTable}, #{teacherName}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "classid")
    void insertTimetable(Timetable timetable);

    /**
     * 更新课程
     */
    @Update("UPDATE timetable SET status = #{status}, exam_table = #{examTable} WHERE course_name = #{courseName} AND teacher_name = #{teacherName} AND uid = #{uid}")
    void updateTimetable(Timetable timetable);
    
    /**
     * 根据ID更新课程
     */
    @Update("UPDATE timetable SET course_name = #{courseName}, teacher_name = #{teacherName}, exam_table = #{examTable}, status = #{status} WHERE classid = #{classid}")
    void updateTimetableById(Timetable timetable);

    /**
     * 根据用户ID查询课程列表
     */
    @Select("SELECT * FROM timetable WHERE uid = #{uid} ORDER BY exam_table ASC")
    List<Timetable> selectByUid(Integer uid);

    /**
     * 更新课程状态
     */
    @Update("UPDATE timetable SET status = #{status} WHERE classid = #{classid}")
    void updateStatus(Integer classid, Integer status);

    /**
     * 根据用户ID删除所有课程
     */
    @Delete("DELETE FROM timetable WHERE uid = #{uid}")
    void deleteByUid(Integer uid);

    /**
     * 根据课程ID删除课程
     */
    @Delete("DELETE FROM timetable WHERE classid = #{classid}")
    void deleteById(Integer classid);
}
