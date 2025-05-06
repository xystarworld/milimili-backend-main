package com.nebulaxy.milimilibackendmain.entity;

import java.util.Date;

public class ContentCount {
    private Integer count_id;
    private Integer video_audit_count;
    private Integer post_audit_count;
    private Integer data_audit_count;
    private Date last_record_time;

    public Integer getCount_id() {
        return count_id;
    }

    public void setCount_id(Integer count_id) {
        this.count_id = count_id;
    }

    public Integer getVideo_audit_count() {
        return video_audit_count;
    }

    public void setVideo_audit_count(Integer video_audit_count) {
        this.video_audit_count = video_audit_count;
    }

    public Integer getPost_audit_count() {
        return post_audit_count;
    }

    public void setPost_audit_count(Integer post_audit_count) {
        this.post_audit_count = post_audit_count;
    }

    public Integer getData_audit_count() {
        return data_audit_count;
    }

    public void setData_audit_count(Integer data_audit_count) {
        this.data_audit_count = data_audit_count;
    }

    public Date getLast_record_time() {
        return last_record_time;
    }

    public void setLast_record_time(Date last_record_time) {
        this.last_record_time = last_record_time;
    }
}