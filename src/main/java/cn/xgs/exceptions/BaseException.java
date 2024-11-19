package cn.xgs.exceptions;


import cn.xgs.exceptions.enums.ExceptionEnum;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * BaseException 基础异常类
 *
 * @author XiaoHu
 */

@Data
public class BaseException extends RuntimeException {

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误信息
     */
    private String message;

    public BaseException(String message) {
        super(message);
        this.message = message;
        this.code = ExceptionEnum.SYSTEM_ERROR_B0001.getCode();
    }
    public BaseException(String code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }
    public BaseException(ExceptionEnum exp) {
        super(exp.getMessage());
        this.message = exp.getMessage();
        this.code = exp.getCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BaseException that = (BaseException) o;

        return new EqualsBuilder().append(getCode(), that.getCode()).append(getMessage(), that.getMessage()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getCode()).append(getMessage()).toHashCode();
    }
}
