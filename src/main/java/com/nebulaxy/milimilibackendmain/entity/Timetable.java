package com.nebulaxy.milimilibackendmain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Date;

public class Timetable {



    private Integer classid;
    private Integer uid;
    private String courseName;

    private Date examTable;
    private String teacherName;
    private Integer status;

    public Integer getClassid() {
        return classid;
    }

    public void setClassid(Integer classid) {
        this.classid = classid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getCourse_name() {
        return courseName;
    }

    public void setCourse_name(String courseName) {
        this.courseName = courseName;
    }

    public Date getExam_table() {
        return examTable;
    }

    public void setExam_table(Date examTable) {
        this.examTable = examTable;
    }

    public String getTeacher_name() {
        return teacherName;
    }

    public void setTeacher_name(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
