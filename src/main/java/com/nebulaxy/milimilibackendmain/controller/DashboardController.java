package com.nebulaxy.milimilibackendmain.controller;

import com.nebulaxy.milimilibackendmain.common.Result;
import com.nebulaxy.milimilibackendmain.service.DashboardService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    /**
     * 获取首页统计数据
     */
    @GetMapping("/stats")
    public Result getStats() {
        Map<String, Object> stats = dashboardService.getStats();
        return Result.success(stats);
    }

    /**
     * 获取近7天数据统计
     */
    @GetMapping("/weeklyStats")
    public Result getWeeklyStats() {
        Map<String, Object> weeklyStats = dashboardService.getWeeklyStats();
        return Result.success(weeklyStats);
    }

    /**
     * 获取内容分布数据
     */
    @GetMapping("/contentDistribution")
    public Result getContentDistribution() {
        try {
            Map<String, Object> distribution = dashboardService.getContentDistribution();
            return Result.success(distribution);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取内容分布数据失败: " + e.getMessage());
        }
    }
}