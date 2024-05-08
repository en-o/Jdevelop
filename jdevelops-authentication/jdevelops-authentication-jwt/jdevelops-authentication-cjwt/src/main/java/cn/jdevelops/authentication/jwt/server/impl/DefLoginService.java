package cn.jdevelops.authentication.jwt.server.impl;

import cn.jdevelops.authentication.jwt.server.LoginService;
import cn.jdevelops.authentication.jwt.util.JwtWebUtil;
import cn.jdevelops.authentication.jwt.vo.TokenSign;
import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.entity.SignEntity;
import cn.jdevelops.util.jwt.exception.LoginException;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录工具
 *
 * @author tan
 */
@ConditionalOnMissingBean(LoginService.class)
public class DefLoginService implements LoginService {

    Logger logger = LoggerFactory.getLogger(DefLoginService.class);


    @Override
    public <T, S extends SignEntity<T>> TokenSign login(S subject) {
        try {
            return new TokenSign(JwtService.generateToken(subject));
        } catch (JoseException e) {
            throw new LoginException("登录异常，请重新登录", e);
        }
    }

    @Override
    public boolean isLogin(HttpServletRequest request) {
        return isLogin(request, false);
    }

    @Override
    public boolean isLogin(String subject) {
        logger.warn("jwt登录方式无法使用此方法进行登录验证");
        return false;
    }

    @Override
    public boolean isLogin(HttpServletRequest request, Boolean cookie) {
        try {
            String token = JwtWebUtil.getToken(request, cookie);
            return JwtService.validateTokenByBoolean(token);
        } catch (Exception e) {
            logger.warn("登录失效", e);
        }
        return false;
    }
}
