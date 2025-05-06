package com.nebulaxy.milimilibackendmain.common;


import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.nebulaxy.milimilibackendmain.entity.UserInformationInfo;
import com.nebulaxy.milimilibackendmain.exception.CustomerException;
import com.nebulaxy.milimilibackendmain.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.从请求头拿到token
        String token =  request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            //如果没拿到，再从参数里拿一次
            token = request.getParameter("token");
        }

        // 2.认证token
        if (StrUtil.isBlank(token)) {
            throw new CustomerException("401","您无权限操作");
        }
        UserInformationInfo account = null;
        try {
            // 拿到token 的载荷数据
            String audience = JWT.decode(token).getAudience().get(0);
            String[] split = audience.split("-");
            String uId = split[0];
            String role = split[1];
            if ("ADMIN".equals(role)) {
                account = userService.selectByUid(uId);
            } else if ("USER".equals(role)) {
                account =userService.selectByUid(uId);
            }
        }catch (Exception e) {
            throw new CustomerException("401","您无权限操作");
        }
        if (account == null){
            throw new CustomerException("401","您无权限操作");
        }

        // 3.验证签名
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token);
        } catch (Exception e){
            throw new CustomerException("401","您无权限操作");
        }
        return true;
    }
}
