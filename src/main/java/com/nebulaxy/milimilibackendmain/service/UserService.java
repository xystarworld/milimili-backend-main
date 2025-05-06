package com.nebulaxy.milimilibackendmain.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nebulaxy.milimilibackendmain.entity.UserInformationInfo;
import com.nebulaxy.milimilibackendmain.exception.CustomerException;
import com.nebulaxy.milimilibackendmain.mapper.UserInformationInfoMapper;
import com.nebulaxy.milimilibackendmain.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Resource
    UserInformationInfoMapper userInformationInfoMapper;

    // 新增管理员信息
    public void addAdmin(UserInformationInfo userInformationInfo) {
        // 根据新账号查询数据库，是否存在同样的账号数据
        UserInformationInfo dbAdmin = userInformationInfoMapper
                .selectAdminByUsername(userInformationInfo.getUsername());
        if (dbAdmin != null) {
            throw new CustomerException("账户已注册");
        }
        userInformationInfoMapper.insertAdmin(userInformationInfo);
    }

    // 新增用户信息
    public void addUser(UserInformationInfo userInformationInfo) {
        // 根据新账号查询数据库，是否存在同样的账号数据
        UserInformationInfo dbAdmin = userInformationInfoMapper.selectUserByUsername(userInformationInfo.getUsername());
        if (dbAdmin != null) {
            throw new CustomerException("邮箱重复");
        }
        userInformationInfoMapper.insertUser(userInformationInfo);
    }

    // 修改成员信息
    public void updateUsers(UserInformationInfo userInformationInfo) {
        userInformationInfoMapper.updateByUid(userInformationInfo);
    }

    // 删除成员信息
    public void deleteUsers(Integer uid) {
        userInformationInfoMapper.deleteByUid(uid);
    }

    // 查询全部成员
    public List<UserInformationInfo> selectAll() {
        return userInformationInfoMapper.selectAll();
    }

    // 分页查询全部成员
    public PageInfo<UserInformationInfo> selectPage(Integer pageNum, Integer pageSize) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<UserInformationInfo> list = userInformationInfoMapper.selectAll();
        return PageInfo.of(list);
    }

    // 分页查询用户
    public PageInfo<UserInformationInfo> selectPageUsers(Integer pageNum, Integer pageSize, String nickname) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<UserInformationInfo> list = userInformationInfoMapper.selectUsers(nickname);
        return PageInfo.of(list);
    }

    // 分页查询全部管理员
    public PageInfo<UserInformationInfo> selectPageAdmin(Integer pageNum, Integer pageSize, String nickname) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<UserInformationInfo> list = userInformationInfoMapper.selectAdmin(nickname);
        return PageInfo.of(list);
    }

    // 依据Uid查询用户
    public UserInformationInfo selectByUid(String uId) {
        return userInformationInfoMapper.selectByUid(uId);
    }

    // 用户登陆系统
    public UserInformationInfo userLogin(UserInformationInfo userInformationInfo) {
        // 验证账号是否存在
        UserInformationInfo dbUser = userInformationInfoMapper.selectUserByUsername(userInformationInfo.getUsername());
        if (dbUser == null) {
            throw new CustomerException("账号不存在");
        }
        // 验证密码是否正确
        if (!dbUser.getPassword().equals(userInformationInfo.getPassword())) {
            throw new CustomerException("账号或密码错误");
        }
        // 创建token并返回前端
        String token = TokenUtils.createToken(dbUser.getUid() + "-" + "USER", dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    // 管理员登陆系统
    public UserInformationInfo adminLogin(UserInformationInfo userInformationInfo) {
        // 验证账号是否存在
        UserInformationInfo dbUser = userInformationInfoMapper.selectAdminByUsername(userInformationInfo.getUsername());
        if (dbUser == null) {
            throw new CustomerException("账号不存在");
        }
        // 验证密码是否正确
        if (!dbUser.getPassword().equals(userInformationInfo.getPassword())) {
            throw new CustomerException("账号或密码错误");
        }
        // 创建token并返回前端
        String token = TokenUtils.createToken(dbUser.getUid() + "-" + "ADMIN", dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

}
