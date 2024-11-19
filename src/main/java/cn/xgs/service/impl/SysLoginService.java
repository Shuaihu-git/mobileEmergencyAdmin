package cn.xgs.service.impl;

import cn.xgs.auth.AuthenticationContextHolder;
import cn.xgs.constants.UserConstants;
import cn.xgs.domain.LoginUser;
import cn.xgs.domain.response.Result;
import cn.xgs.service.ISysUserService;
import cn.xgs.utils.JwtUtils;
import cn.xgs.utils.RedisCache;
import cn.xgs.utils.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static cn.xgs.exceptions.enums.ExceptionEnum.exception;

@Component
@Slf4j
public class SysLoginService {
    @Autowired
    private JwtUtils jwtUtils;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid, String backendPublicKey) {
        // 验证码校验
//        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try {
//            RSAUtils.RSAKeyPair rsaKeyPair = redisCache.getCacheObject(RedisConstants.LOGIN_PUBLIC_AND_PRIVATE_KEY+Constants.PUBLIC_AND_PRIVATE_KEY);
//            if (StringUtil.isNull(rsaKeyPair)){
//                exception("公钥已过期,请重新刷新");
//            }
            //RSAUtils.decryptByPrivateKey(rsaKeyPair.getBackendPrivateKey(),password)
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                log.error("用户名或密码错误", e);
                exception("用户名或密码错误");
                return "用户名或密码错误";
            } else {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                log.error("登录异常", e);
                exception(e.getMessage());
                return "用户名或密码错误";
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
//        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        recordLoginInfo(loginUser.getUser().getUserId());
        // 生成token
        return jwtUtils.createToken(loginUser);
    }
    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            exception("用户名或密码错误");
        }
        // 密码如果不在指定范围内 错误
//        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
//                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
//            exception("密码不在指定范围内");
//        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            exception("用户名不在指定范围内");
        }
    }

}
