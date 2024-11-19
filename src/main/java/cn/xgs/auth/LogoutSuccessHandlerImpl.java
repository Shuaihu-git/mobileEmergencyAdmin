package cn.xgs.auth;

import cn.xgs.domain.LoginUser;
import cn.xgs.domain.response.Result;
import cn.xgs.utils.JwtUtils;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Resource
    private JwtUtils jwtUtils;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        try {
            LoginUser loginUser = jwtUtils.getLoginUser(request);
            {
                // 删除用户缓存记录
                jwtUtils.delLoginUser(loginUser.getToken());
            }
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSONObject.toJSONString(Result.success("用户注销成功")));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
