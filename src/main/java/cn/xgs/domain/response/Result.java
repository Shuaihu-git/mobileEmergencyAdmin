package cn.xgs.domain.response;

import cn.xgs.exceptions.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author XiaoHu
 * 统一响应体
 */
@Data
@AllArgsConstructor
public class Result<T> {

    /**
     * 状态码
     */
    private String code;

    /**
     * 返回内容
     */
    private String msg;

    /**
     * 数据对象
     */
    private T data;

    public static <T> Result<T> success(String code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    public static <T> Result<T> success(String code, String msg) {
        return success(code, msg, null);
    }

    public static <T> Result<T> success(String msg, T data) {
        return success(ExceptionEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> Result<T> success(T data) {
        return success(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(ExceptionEnum exceptionEnum, T data) {
        return success(exceptionEnum.getCode(), exceptionEnum.getMessage(), data);
    }

    public static <T> Result<T> success() {
        return success(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> error(String code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    public static <T> Result<T> error(String code, String msg) {
        return error(code, msg, null);
    }

    public static <T> Result<T> error(String msg) {
        return error(ExceptionEnum.SYSTEM_ERROR_B0001.getCode(), msg, null);
    }

    public static <T> Result<T> error(ExceptionEnum exceptionEnum, T data) {
        return error(exceptionEnum.getCode(), exceptionEnum.getMessage(), data);
    }

    public static <T> Result<T> error(ExceptionEnum exceptionEnum) {
        return error(exceptionEnum, null);
    }

    public static <T> Result<T> error() {
        return error(ExceptionEnum.SYSTEM_ERROR_B0001, null);
    }

}
