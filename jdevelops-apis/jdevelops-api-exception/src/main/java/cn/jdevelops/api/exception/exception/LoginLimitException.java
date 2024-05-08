package cn.jdevelops.api.exception.exception;

import cn.jdevelops.api.result.emums.ExceptionCode;

/**
 * 登录限制异常
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-12 09:33
 */
public class LoginLimitException extends BusinessException {
    public LoginLimitException(int code, String message) {
        super(code, message);
    }

    public LoginLimitException(String message) {
        super(message);
    }

    public LoginLimitException(ExceptionCode errorCode) {
        super(errorCode.getCode(), errorCode.getMessage());
    }
}
