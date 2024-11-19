package cn.xgs.constants;

import io.jsonwebtoken.Claims;

public class RedisConstants {
    /**
     * 统一key前缀
     */
    public static final String PREFIX = "mobile-emergency:";
    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY =  PREFIX + "captcha_codes:";
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = PREFIX + "login_tokens:";
    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 登录公私钥 redis key(backendPrivateKey)
     */
    public static final String LOGIN_PUBLIC_AND_PRIVATE_KEY = PREFIX + "login_public_and_private_key:";
    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = PREFIX + "pwd_err_cnt:";
}
