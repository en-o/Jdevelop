package cn.jdevelops.api.exception.exception;

import cn.jdevelops.api.result.emums.ExceptionCode;
import cn.jdevelops.api.result.emums.ResultCode;

/**
 * 自定义异常
 * @author tn
 * @version 1
 * @date 2020/6/17 15:07
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 4129812562603997310L;

    private int code;

    /**
     * 是否修改 http请求的status， 默认false以全局为主, true=code
     */
    private Boolean  httpServletResponseStatus;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.FAIL.getCode();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ExceptionCode code, String message) {
        super(message);
        this.code = code.getCode();
    }

    public BusinessException(ExceptionCode exceptionCode){
        super(exceptionCode.getMessage());
        this.code = exceptionCode.getCode();
    }




    public int getCode() {
        return this.code;
    }


    public Boolean getHttpServletResponseStatus() {
        if(httpServletResponseStatus == null){
            return false;
        }else {
            return httpServletResponseStatus;
        }
    }

    /**
     * 否修改 http请求的status，
     * @param httpServletResponseStatus 默认false以全局为主, true=code
     * @return BusinessException
     */
    public BusinessException setHttpServletResponseStatus(Boolean httpServletResponseStatus) {
        this.httpServletResponseStatus = httpServletResponseStatus;
        return this;
    }

    public String getErrorMessage() {
        return super.getMessage();
    }
}
