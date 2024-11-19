package cn.xgs.exceptions.enums;


import cn.xgs.exceptions.BaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常枚举
 * 错误码：
 * 1. 五位组成
 * 2. A代表用户端错误
 * 3. B代表当前系统异常
 * 4. C代表第三方服务异常
 * 4. 若无法确定具体错误，选择宏观错误
 * 6. 大的错误类间的步长间距预留100
 * @author XiaoHu
 */
@Getter
@AllArgsConstructor
public enum ExceptionEnum {


    /**
     * 成功
     */
    SUCCESS("00000", "成功"),

    /**
     * 一级宏观错误码 -用户
     */
    USER_ERROR_A0001("A0001", "错误的用户数据"),

    /**
     * 二级宏观错误码 -用户
     */
    USER_ERROR_A0100("A0100", "用户不存在"),

    /**
     * 一级宏观错误码 -系统
     */
    SYSTEM_ERROR_B0001("B0001", "系统内部异常"),
    SYSTEM_ERROR_B0002("B0002", "用户注册错误"),
    SYSTEM_ERROR_B0003("B0003", "权限校验失败"),
    SYSTEM_ERROR_B0004("B0004", "阿里云服务异常"),
    SYSTEM_ERROR_B0005("B0005", "短息发送失败"),
    SYSTEM_ERROR_B0006("B0006", "验证码已发送"),

    /**
     * 二级宏观错误码 -系统
     */
    SYSTEM_ERROR_B0100("B0100", "用户注册错误"),
    SYSTEM_ERROR_B0101("B0101", "获取用户ID异常"),
    SYSTEM_ERROR_B0102("B0102", "获取部门ID异常"),
    SYSTEM_ERROR_B0103("B0103", "获取用户账户异常"),
    SYSTEM_ERROR_B0104("B0104", "获取用户信息异常"),
    SYSTEM_ERROR_B0105("B0105", "本月无项目归集"),
    SYSTEM_ERROR_B0106("B0106", "表头为空"),
    SYSTEM_ERROR_B0107("B0107", "表头字段已更新，请刷新页面"),
    SYSTEM_ERROR_B0108("B0108", "请勿重复创建！"),
    /**
     * 二级宏观错误码 -系统-模板
     */
    SYSTEM_ERROR_B0200("B0200", "无法新增同名字段"),
    SYSTEM_ERROR_B0201("B0201", "无法修改为已存在字段名"),
    SYSTEM_ERROR_B0202("B0202", "未知的字段格式"),
    SYSTEM_ERROR_B0203("B0203", "字段设置已更新，请先同步到最新数据"),
    SYSTEM_ERROR_B0204("B0204", "系统功能字段不能删除"),
    SYSTEM_ERROR_B0205("B0205", "系统功能字段不能修改"),
    SYSTEM_ERROR_B0206("B0206", "系统默认模板配置丢失"),


    /**
     * 二级宏观错误码 -软著
     */
    SYSTEM_ERROR_B0300("B0300", "无软著申请信息"),
    SYSTEM_ERROR_B0301("B0301", "归档失败"),
    SYSTEM_ERROR_B0302("B0302", "已归档"),
    /**
     * 二级宏观错误码 -文件
     */
    SYSTEM_ERROR_D0001("D0001", "导出失败"),

    /**
     * 一级宏观错误码 -三方
     */
    SERVICE_ERROR_C0001("C0001", "第三方服务出错");
    private final String code;

    private final String message;

    public static void exception(ExceptionEnum exceptionEnum) {
        exception(exceptionEnum.getCode(), exceptionEnum.getMessage());
    }

    /**
     * @param exceptionEnum 异常枚举
     * @param otherMsg      需要拼接的额外信息
     */
    public static void exception(ExceptionEnum exceptionEnum, String otherMsg) {
        exception(exceptionEnum.getCode(), exceptionEnum.getMessage() + ", " + otherMsg);
    }

    public static void exception(String code, String message) {
        throw new BaseException(code, message);
    }

    public static void exception(String message) {
        throw new BaseException(message);
    }

}
