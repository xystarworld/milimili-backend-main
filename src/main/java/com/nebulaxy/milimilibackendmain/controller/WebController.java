package com.nebulaxy.milimilibackendmain.controller;

import com.nebulaxy.milimilibackendmain.common.Result;
import com.nebulaxy.milimilibackendmain.entity.UserInformationInfo;
import com.nebulaxy.milimilibackendmain.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WebController {

    @Resource
    UserService userService;

    @GetMapping("/")//接口的路径，全局唯一
    public Result hello() {
        return Result.success();
    }

    /**
     * 登录
     */
    @PostMapping("/adminLogin")
    public Result AdminLogin(@RequestBody UserInformationInfo userInformationInfo) {
        UserInformationInfo dbUser = userService.adminLogin(userInformationInfo);
        return Result.success(dbUser);
    }

    @PostMapping("/userLogin")
    public Result UserLogin(@RequestBody UserInformationInfo userInformationInfo) {
        UserInformationInfo dbUser = userService.userLogin(userInformationInfo);
        return Result.success(dbUser);
    }
}
