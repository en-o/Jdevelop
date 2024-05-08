package cn.jdevelops.api.exception.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 异常配置
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/8/7 10:00
 */
@ConfigurationProperties(prefix = "jdevelops.exception")
@Component
public class ExceptionConfig {

    /**
     * 日志打印 (默认级别为ERROR)
     * 默认true 打印
     */
    private Boolean logInput;


    /**
     * 是否设置 HttpServletResponse.status且跟自定义的code同步
     *  默认 false 不设置
     */
    private Boolean httpServletResponseStatus;

    /**
     * 设置 content-type 默认 application/json;charset=UTF-8
     */
    private String httpServletResponseHeaderContentType;


    public ExceptionConfig() {
    }

    public ExceptionConfig(Boolean logInput, Boolean httpServletResponseStatus, String httpServletResponseHeaderContentType) {
        this.logInput = logInput;
        this.httpServletResponseStatus = httpServletResponseStatus;
        this.httpServletResponseHeaderContentType = httpServletResponseHeaderContentType;
    }

    @Override
    public String toString() {
        return "ExceptionConfig{" +
                "logInput=" + logInput +
                ", HttpServletResponseStatus=" + httpServletResponseStatus +
                ", HttpServletResponseHeaderContentType='" + httpServletResponseHeaderContentType + '\'' +
                '}';
    }

    public Boolean getLogInput() {
        if(null==logInput){
            return true;
        }
        return logInput;
    }

    public void setLogInput(Boolean logInput) {
        this.logInput = logInput;
    }

    public Boolean getHttpServletResponseStatus() {
        if(null== httpServletResponseStatus){
            return false;
        }
        return httpServletResponseStatus;
    }

    public void setHttpServletResponseStatus(Boolean httpServletResponseStatus) {
        this.httpServletResponseStatus = httpServletResponseStatus;
    }

    public String getHttpServletResponseHeaderContentType() {
        return httpServletResponseHeaderContentType;
    }

    public void setHttpServletResponseHeaderContentType(String httpServletResponseHeaderContentType) {
        this.httpServletResponseHeaderContentType = httpServletResponseHeaderContentType;
    }
}
