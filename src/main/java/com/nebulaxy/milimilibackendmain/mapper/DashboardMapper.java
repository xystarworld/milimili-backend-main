package com.nebulaxy.milimilibackendmain.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DashboardMapper {
    
    // 获取待审核视频数量
    int countPendingVideos();
    
    // 获取待审核帖子数量
    int countPendingPosts();
    
    // 获取待审核资料数量
    int countPendingData();
    
    // 获取视频总数
    int countTotalVideos();
    
    // 获取帖子总数
    int countTotalPosts();
    
    // 获取资料总数
    int countTotalData();
    
    // 获取近7天的视频数据
    List<Map<String, Object>> getVideoDataLast7Days();
    
    // 获取近7天的帖子数据
    List<Map<String, Object>> getPostDataLast7Days();
    
    // 获取近7天的资料数据
    List<Map<String, Object>> getDataLast7Days();
}