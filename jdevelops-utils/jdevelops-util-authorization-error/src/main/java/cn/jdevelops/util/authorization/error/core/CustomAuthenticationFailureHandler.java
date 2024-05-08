package cn.jdevelops.util.authorization.error.core;

import cn.jdevelops.util.authorization.error.respone.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义AuthenticationFailureHandler 处理 authorization中的一些异常
 * @author tan
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        ResponseUtil.exceptionResponse(response, exception);
    }
}
