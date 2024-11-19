package cn.xgs.utils;

import cn.xgs.constants.Constants;
import cn.xgs.constants.RedisConstants;
import cn.xgs.domain.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author XiaoHu
 * JWT工具类
 **/
@Slf4j
@Component
@Data
public class JwtUtils {
    @Autowired
    private RedisCache redisCache;
    public static final Long JWT_TTL = 60*60*1000L;
    public static final String JWT_KEY= "jwtHyperWAF";
    // 令牌有效期（默认30分钟）
    @Value("${user.column.expire-time}")
    private int expireTime;

    private long time;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    protected static final int SECOND_MINUTE = 60;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
    public static String createJwt(String subject){
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        return builder.compact();
    }
    public static String createJwt(String subject,Long ttlMillis){
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());
        return builder.compact();
    }
    private static JwtBuilder getJwtBuilder(String subject,Long ttlMillis,String uuid){
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generaKey();
        long millis = System.currentTimeMillis();
        Date startDate = new Date(millis);
        if(ttlMillis==null){
            ttlMillis=JwtUtils.JWT_TTL;
        }
        long expMills = millis + ttlMillis;
        Date expDate = new Date(expMills);
        return Jwts.builder().setId(uuid)
                .setSubject(subject)
                .setIssuer("admin")
                .setIssuedAt(startDate)
                .signWith(algorithm,secretKey)
                .setExpiration(expDate);
    }
    public static String createJwt(String id,String subject,Long ttlMills){
        JwtBuilder builder = getJwtBuilder(subject, ttlMills, id);
        return builder.compact();
    }

    private static SecretKey generaKey() {
        byte[] decode = Base64.getDecoder().decode(JwtUtils.JWT_KEY);
        SecretKey key = new SecretKeySpec(decode, 0, decode.length,"AES");
        log.info("{}",key);
        return key;
    }
    public static Claims praseJwt(String Jwt) throws Exception{
        SecretKey secretKey = generaKey();
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(Jwt)
                .getBody();
    }
    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                String uuid = praseJwt(token).getSubject();
                // 解析对应的权限以及用户信息
                String userKey = getTokenKey(uuid);
                return redisCache.getCacheObject(userKey);
            } catch (Exception e) {
                log.warn("用户信息解析失败", e);
            }
        }
        return null;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            try {
                String uuid = praseJwt(token).getSubject();
                // 解析对应的权限以及用户信息
                String userKey = getTokenKey(uuid);
                return redisCache.getCacheObject(userKey);
            } catch (Exception e) {
                log.warn("用户信息解析失败", e);
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (Objects.nonNull(loginUser) && Objects.isNull(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = cn.hutool.core.lang.UUID.fastUUID().toString();
        loginUser.setToken(token);
//        setUserAgent(loginUser);
        refreshToken(loginUser);

        return createJwt(token);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        refreshToken(loginUser);
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

//    /**
//     * 设置用户代理信息
//     *
//     * @param loginUser 登录信息
//     */
//    public void setUserAgent(LoginUser loginUser)
//    {
//        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
//        String ip = IpUtils.getIpAddr();
//        loginUser.setIpaddr(ip);
//        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
//        loginUser.setBrowser(userAgent.getBrowser().getName());
//        loginUser.setOs(userAgent.getOperatingSystem().getName());
//    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return RedisConstants.LOGIN_TOKEN_KEY + uuid;
    }

    public static void main(String[] args) throws Exception {
//        String jwt = createJwt("123456");
//        log.info("{}",jwt.length());
//        Claims claims = praseJwt(jwt);
//        claims.getSubject();
//        log.info("{}",jwt);
//        log.info("{}",claims.getSubject());
    }
}