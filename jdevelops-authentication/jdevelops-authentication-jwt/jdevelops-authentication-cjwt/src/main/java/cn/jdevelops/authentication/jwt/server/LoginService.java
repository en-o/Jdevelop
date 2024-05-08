package cn.jdevelops.authentication.jwt.server;

import cn.jdevelops.authentication.jwt.vo.TokenSign;
import cn.jdevelops.util.jwt.entity.SignEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 登录工具
 * @author tan
 */
public interface LoginService  {


      Logger logger = LoggerFactory.getLogger(LoginService.class);


    /**
     * 登录
     *  - 信息保存到redis中
     * @param subject 用户唯一凭证(一般是登录名
     * @return 签名
     */
    <T,S extends SignEntity<T>> TokenSign login(S subject);

    /**
     * 是否登录
     *
     * @param request HttpServletRequest
     * @return true 登录中
     */
     boolean isLogin(HttpServletRequest request);

    /**
     * 是否登录
     *
     * @param subject 一般是用户登录名，主要看你登陆时存的  subject是什么
     * @return true 登录中
     */
    boolean isLogin(String subject);


    /**
     * 是否登录
     *
     * @param request HttpServletRequest
     * @param cookie  true 去cookie参数
     * @return true 登录中
     */
    boolean isLogin(HttpServletRequest request, Boolean cookie);


    /**
     * 退出登录
     * @param request HttpServletRequest
     */
    default void loginOut(HttpServletRequest request){}

    /**
     * 退出登录
     * @param subject subject
     */
    default void loginOut(String subject){}


    /**
     * 退出登录
     * @param subject subject
     */
    default void loginOut(List<String> subject){}

}
