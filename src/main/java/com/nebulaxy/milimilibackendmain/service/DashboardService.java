package com.nebulaxy.milimilibackendmain.service;

import com.nebulaxy.milimilibackendmain.mapper.DashboardMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DashboardService {
    
    @Resource
    private DashboardMapper dashboardMapper;
    
    /**
     * 获取首页统计数据
     */
    public Map<String, Object> getStats() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取待审核视频数量
        int pendingVideos = dashboardMapper.countPendingVideos();
        
        // 获取待审核帖子数量
        int pendingPosts = dashboardMapper.countPendingPosts();
        
        // 获取待审核资料数量
        int pendingData = dashboardMapper.countPendingData();
        
        result.put("pendingVideos", pendingVideos);
        result.put("pendingPosts", pendingPosts);
        result.put("pendingMaterials", pendingData);
        
        return result;
    }
    
    /**
     * 获取近7天数据统计
     */
    public Map<String, Object> getWeeklyStats() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取近7天的日期
        List<String> days = getLast7Days();
        result.put("days", days);
        
        // 初始化数据数组
        Integer[] videoData = new Integer[7];
        Integer[] postData = new Integer[7];
        Integer[] materialData = new Integer[7];
        Arrays.fill(videoData, 0);
        Arrays.fill(postData, 0);
        Arrays.fill(materialData, 0);
        
        // 获取近7天的视频数据
        List<Map<String, Object>> videoStats = dashboardMapper.getVideoDataLast7Days();
        fillDataArray(videoStats, videoData, days);
        
        // 获取近7天的帖子数据
        List<Map<String, Object>> postStats = dashboardMapper.getPostDataLast7Days();
        fillDataArray(postStats, postData, days);
        
        // 获取近7天的资料数据
        List<Map<String, Object>> dataStats = dashboardMapper.getDataLast7Days();
        fillDataArray(dataStats, materialData, days);
        
        result.put("videoData", videoData);
        result.put("postData", postData);
        result.put("materialData", materialData);
        
        return result;
    }
    
    /**
     * 获取内容分布数据
     */
    /**
     * 获取内容分布数据
     */
    public Map<String, Object> getContentDistribution() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取视频总数
        int totalVideos = dashboardMapper.countTotalVideos();
        
        // 获取帖子总数
        int totalPosts = dashboardMapper.countTotalPosts();
        
        // 获取资料总数
        int totalData = dashboardMapper.countTotalData();
        
        result.put("videoCount", totalVideos);
        result.put("postCount", totalPosts);
        result.put("materialCount", totalData);
        
        return result;
    }
    
    /**
     * 获取近7天的日期
     */
    private List<String> getLast7Days() {
        List<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Calendar calendar = Calendar.getInstance();
        
        for (int i = 6; i >= 0; i--) {
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            result.add(sdf.format(calendar.getTime()));
        }
        
        return result;
    }
    
    /**
     * 填充数据数组
     */
    private void fillDataArray(List<Map<String, Object>> dataList, Integer[] dataArray, List<String> days) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd");
        
        for (Map<String, Object> data : dataList) {
            try {
                String dateStr = (String) data.get("date");
                Date date = inputFormat.parse(dateStr);
                String formattedDate = outputFormat.format(date);
                
                int index = days.indexOf(formattedDate);
                if (index != -1) {
                    dataArray[index] = ((Number) data.get("count")).intValue();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}