package com.nebulaxy.milimilibackendmain.entity;

import java.sql.Timestamp;

/**
 * 作品信息
 */
public class Content {
    private String vid;
    private Integer uid;
    private String title;
    private Double duration;
    private String mcId;
    private String scId;
    private String tags;
    private String descr;
    private String topic;
    private String coverUrl;
    private String videoUrl;
    private Integer status;
    private Timestamp uploadDate;
    private Timestamp deleteDate;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }



    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getMcId() {
        return mcId;
    }

    public void setMcId(String mcId) {
        this.mcId = mcId;
    }

    public String getScId() {
        return scId;
    }

    public void setScId(String scId) {
        this.scId = scId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Timestamp getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Timestamp deleteDate) {
        this.deleteDate = deleteDate;
    }


    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
