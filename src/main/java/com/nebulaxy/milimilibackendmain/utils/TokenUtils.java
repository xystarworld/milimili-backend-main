package com.nebulaxy.milimilibackendmain.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nebulaxy.milimilibackendmain.entity.UserInformationInfo;
import com.nebulaxy.milimilibackendmain.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Component
public class TokenUtils {

    @Resource
    UserService userService;

    static UserService staticUserService;

    //Springboot启动后会加载
    @PostConstruct
    public void init() {
        staticUserService = userService;
    }

    /**
     * 生成token
     */
    public static String createToken(String data,String sign) {
        return JWT.create().withAudience(data) // 将 uid-role 保存到 token 里面，作为载荷
                .withExpiresAt(DateUtil.offsetDay(new Date(), 1)) // 一天后 token 过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥， HMAC256 算法加密
    }

    /**
     * 获取当前登录的用户信息
     */
    public static UserInformationInfo getCurrentUser(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)){
            token = request.getParameter("token");
        }
        // 拿到token 的载荷数据
        String audience = JWT.decode(token).getAudience().get(0);
        String[] split = audience.split("-");
        String uId = split[0];
        String role = split[1];
        if ("ADMIN".equals(role)) {
            return staticUserService.selectByUid(uId);
        } else if ("USER".equals(role)) {
            return staticUserService.selectByUid(uId);
        }
        return null;
    }
}
