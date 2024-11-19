package cn.xgs.utils;

import cn.xgs.domain.LoginUser;
import cn.xgs.exceptions.BaseException;
import cn.xgs.exceptions.enums.ExceptionEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static cn.xgs.exceptions.enums.ExceptionEnum.exception;

public class SecurityUtils {
    /**
     * 用户ID
     **/
    public static Long getUserId() {
        try {
            return getLoginUser().getUser().getUserId();
        } catch (Exception e) {
            exception(ExceptionEnum.SYSTEM_ERROR_B0101);
        }
        return null;
    }

    /**
     * 获取部门ID
     **/
    public static Long getDeptId() {
        try {
            return getLoginUser().getDeptId();
        } catch (Exception e) {
            exception(ExceptionEnum.SYSTEM_ERROR_B0102);
        }
        return null;
    }

    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            exception(ExceptionEnum.SYSTEM_ERROR_B0103);
        }
        return null;
    }

    /**
     * 获取用户账户
     **/
    public static String getNickName() {
        try {
            return getLoginUser().getUser().getNickName();
        } catch (Exception e) {
            exception(ExceptionEnum.SYSTEM_ERROR_B0103);
        }
        return null;
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BaseException(ExceptionEnum.SYSTEM_ERROR_B0104);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * 是否为管理员
     *
     * @return 结果
     */
    public static boolean isAdmin() {
        return isAdmin(getUserId());
    }

}