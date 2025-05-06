package com.nebulaxy.milimilibackendmain.controller;

import com.github.pagehelper.PageInfo;
import com.nebulaxy.milimilibackendmain.common.Result;
import com.nebulaxy.milimilibackendmain.entity.UserInformationInfo;
import com.nebulaxy.milimilibackendmain.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    UserService userService;

    /**
     * 新增管理员
     */
    @PostMapping("/adminAdd")
    public Result addAdmin(@RequestBody UserInformationInfo userInformationInfo) {
        //@RequestBody 接收前端传来的 json 数据
        userService.addAdmin(userInformationInfo);
        return Result.success();
    }

    /**
     * 注册用户
     */
    @PostMapping("/userAdd")
    public Result addUser(@RequestBody UserInformationInfo userInformationInfo) {
        //@RequestBody 接收前端传来的 json 数据
        userService.addUser(userInformationInfo);
        return Result.success();
    }

    /**
     * 修改用户/管理员
     */
    @PutMapping("/usersUpdate")
    public Result updateUsers(@RequestBody UserInformationInfo userInformationInfo) {
        //@RequestBody 接收前端传来的 json 数据
        userService.updateUsers(userInformationInfo);
        return Result.success();
    }

    /**
     * 删除用户/管理员
     */
    @DeleteMapping("/usersDelete/{uid}")
    public Result deleteUsers(@PathVariable Integer uid) {
        //@PathVariable 接收前端传来的路径参数
        userService.deleteUsers(uid);
        return Result.success();
    }

    /**
     * 搜索所有成员
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<UserInformationInfo> userInformationInfoList = userService.selectAll();
        return Result.success(userInformationInfoList);
    }

    /**
     * 分页查询
     * pageNum 当前页码
     * pageSize 每页展示数量
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "15") Integer pageSize) {
        PageInfo<UserInformationInfo> pageInfo = userService.selectPage(pageNum,pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 用户分页查询
     */
    @GetMapping("/selectPageUsers")
    public Result selectPageUsers(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "15") Integer pageSize,
                                  @RequestParam(required = false) String nickname) {
        PageInfo<UserInformationInfo> pageInfo = userService.selectPageUsers(pageNum,pageSize,nickname);
        return Result.success(pageInfo);
    }

    /**
     * 管理分页查询
     */
    @GetMapping("/selectPageAdmin")
    public Result selectPageAdmin(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "15") Integer pageSize,
                                  @RequestParam(required = false) String nickname) {
        PageInfo<UserInformationInfo> pageInfo = userService.selectPageAdmin(pageNum,pageSize,nickname);
        return Result.success(pageInfo);
    }

    /**
     * 查找用户
     */
    @GetMapping("/findUser")
    public Result findUser(@RequestParam String uid) {
        return Result.success(userService.selectByUid(uid));
    }

}
