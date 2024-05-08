package cn.jdevelops.authentication.jwt.interceptor;

import cn.jdevelops.api.result.custom.ExceptionResultWrap;
import cn.jdevelops.api.result.emums.TokenExceptionCode;
import cn.jdevelops.authentication.jwt.annotation.ApiMapping;
import cn.jdevelops.authentication.jwt.annotation.ApiPlatform;
import cn.jdevelops.authentication.jwt.annotation.NotRefreshToken;
import cn.jdevelops.authentication.jwt.exception.PermissionsException;
import cn.jdevelops.authentication.jwt.server.CheckTokenInterceptor;
import cn.jdevelops.authentication.jwt.util.CookieUtil;
import cn.jdevelops.authentication.jwt.util.JwtWebUtil;
import cn.jdevelops.authentication.jwt.vo.CheckToken;
import cn.jdevelops.spi.ExtensionLoader;
import cn.jdevelops.util.jwt.config.JwtConfig;
import cn.jdevelops.util.jwt.constant.JwtConstant;
import cn.jdevelops.util.jwt.constant.PlatformConstant;
import cn.jdevelops.util.jwt.core.JwtService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static cn.jdevelops.api.result.emums.TokenExceptionCode.UNAUTHENTICATED_PLATFORM;


/**
 * @author tan
 * @date 2020/4/18 22:12
 */
@Slf4j
public class WebApiInterceptor implements HandlerInterceptor {
    private CheckTokenInterceptor checkTokenInterceptor;

    private final JwtConfig jwtConfig;

    public WebApiInterceptor(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        getCheckTokenInterceptor();
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {

        if (handler instanceof ResourceHttpRequestHandler && jwtConfig.getOss().isEnable()) {
            ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler) handler;
            String servletPath = request.getServletPath();
            log.debug("====> servlet path {}, resourceHandler str {}", servletPath, resourceHandler);
            Integer status = checkOssToken(resourceHandler, servletPath, request, response);
            if (status != null) {
                return status == 1;
            }
        }
        // 放行 非HandlerMethod 请求
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取处理器方法所属的类
        Class<?> controllerClass = handlerMethod.getBeanType();

        Method method = handlerMethod.getMethod();
        Logger logger = LoggerFactory.getLogger(handlerMethod.getBeanType());
        if (method.isAnnotationPresent(ApiMapping.class)) {
            ApiMapping annotation = method.getAnnotation(ApiMapping.class);
            if (annotation.checkToken()) {
                return check_refresh_token(request, response,  method, logger, controllerClass);
            } else {
                return true;
            }
        } else {
            return check_refresh_token(request, response,  method, logger, controllerClass);
        }
    }


    /**
     * 验证并刷新token缓存
     *
     * @param request         request
     * @param response        response
     * @param method          method
     * @param logger          logger
     * @param controllerClass 处理器方法所属的类
     * @return boolean
     * @throws IOException Exception
     */
    private boolean check_refresh_token(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Method method,
                                        Logger logger,
                                        Class<?> controllerClass) throws Exception {
        CheckToken check = check(request, response,  logger, method, controllerClass);
        if (Boolean.TRUE.equals(check.getCheck())) {
            refreshToken(check.getToken(), method);
        }
        return check.getCheck();
    }


    /**
     * 验证token
     *
     * @param request  request
     * @param response response
     * @param logger   logger
     * @param controllerClass 处理器方法所属的类
     * @return check
     * @throws IOException IOException
     */
    private CheckToken check(HttpServletRequest request,
                             HttpServletResponse response,
                             Logger logger,
                             Method method,
                             Class<?> controllerClass) throws Exception {
        String token = JwtWebUtil.getToken(request, jwtConfig.getCookie());
        // 验证token
        boolean flag = checkTokenInterceptor.checkToken(token);
        logger.info("需要验证token,url:{}, 校验结果：{},token:{}", request.getRequestURI(), flag, token);
        if (!flag) {
            extractedErrorResponse(response);
            return new CheckToken(false, token);
        }
        /* 日志用的 - %X{token} */
        MDC.put(JwtConstant.TOKEN, token);
        // 验证接口是否允许被调用
        if (Boolean.TRUE.equals(jwtConfig.getVerifyPlatform())) {
            checkApiPlatform(token, method, controllerClass);
        }
        // 验证用户状态
        checkUserStatus(token);
        // 验证用户接口权限
        if (Boolean.TRUE.equals(jwtConfig.getVerifyPermission())) {
            checkUserPermission(token, method);
        }
        return new CheckToken(true, token);
    }

// =================== 检查接口的使用范围是否能跟token中的返回对应上 ==================================

    /**
     * 检查接口的使用范围是否能跟token中的返回对应上
     *
     * @param token  token
     * @param method method
     * @param controllerClass 处理器方法所属的类
     */
    private void checkApiPlatform(String token,
                                  Method method,
                                  Class<?> controllerClass) {
        List<PlatformConstant> platformConstants = JwtService.getPlatformConstantExpires(token);
        if (method.isAnnotationPresent(ApiPlatform.class)) {
            if (!jwtListExistAnnotationMethod(platformConstants, method)) {
                throw new PermissionsException(UNAUTHENTICATED_PLATFORM);
            }
        }else if(controllerClass.isAnnotationPresent(ApiPlatform.class)
                && (!jwtListExistAnnotationMethodClasses(platformConstants, controllerClass))) {
                throw new PermissionsException(UNAUTHENTICATED_PLATFORM);

        }

    }

    /**
     * 检查方法
     */
    private boolean jwtListExistAnnotationMethod(List<PlatformConstant> platformConstants,
                                           Method method) {
        ApiPlatform annotation = method.getAnnotation(ApiPlatform.class);
        for (PlatformConstant annotationPlatform : annotation.platform()) {
            if (annotationPlatform.contains(platformConstants)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查类
     */
    private boolean jwtListExistAnnotationMethodClasses(List<PlatformConstant> platformConstants,
                                                        Class<?> controllerClass) {
        ApiPlatform annotation = controllerClass.getAnnotation(ApiPlatform.class);
        for (PlatformConstant annotationPlatform : annotation.platform()) {
            if (annotationPlatform.contains(platformConstants)) {
                return true;
            }
        }
        return false;
    }

// =================== 检查接口的使用范围是否能跟token中的返回对应上 ==================================


// =================== 检查用户状态 ==================================

    /**
     * 检查用户状态
     *
     * @param token token
     * @throws Exception 用户状态异常
     */
    private void checkUserStatus(String token) throws Exception {
        // 检查用户状态
        checkTokenInterceptor.checkUserStatus(token);
    }

// =================== 检查token中的role是否跟接口的role匹配 ==================================


    /**
     * 检查token中的role是否跟接口的role匹配
     *
     * @param token token
     * @throws Exception 用户状态异常
     */
    private void checkUserPermission(String token, Method method) throws Exception {
        // 检查用户状态
        checkTokenInterceptor.checkUserPermission(token, method);
    }

    // =================== 检查token中的role是否跟接口的role匹配 ==================================


    /**
     * 刷新缓存 - redis中才有用
     *
     * @param token  token
     * @param method method
     */
    private void refreshToken(String token, Method method) {
        // token缓存刷新
        try {

            // 全局设置刷新状态 false: 不刷新
            if (Boolean.TRUE.equals(jwtConfig.getCallRefreshToken())
                    && (!method.isAnnotationPresent(NotRefreshToken.class))) {
                // 每次接口进来都要属性 token缓存。刷新方式请自主实现
                checkTokenInterceptor.refreshToken(token);
            }
        } catch (Exception e) {
            log.warn("token缓存刷新失败", e);
        }
    }

    private void getCheckTokenInterceptor() {
        checkTokenInterceptor = ExtensionLoader.getExtensionLoader(CheckTokenInterceptor.class).getJoin("defaultInterceptor");
    }


    /**
     * 检查 local oss 文件访问的权限
     *
     * @return 1: 放行, 0 拦截, null 不做处理
     */
    private Integer checkOssToken(ResourceHttpRequestHandler resourceHandler
            , String servletPath
            , HttpServletRequest request
            ,HttpServletResponse response) {

        try {
            ApplicationContext applicationContext = resourceHandler
                    .getApplicationContext();
            if (applicationContext == null) {
                return null;
            }
            Environment environment = applicationContext.getEnvironment();
            String localOssResourceUpDir = environment.getProperty("detabes.oss.local.upload-dir", "");

            String localOssResourceContextPath = environment.getProperty("detabes.oss.local.context-path", "");

            // local oss
            if (servletPath.startsWith(localOssResourceContextPath)) {
                if (resourceHandler.toString().equals("ResourceHttpRequestHandler [URL [file:" + localOssResourceUpDir + "/]]")) {
                    Cookie cookie = CookieUtil.findCookie(
                            jwtConfig.getOss().getOssLocalJwtKey(), request.getCookies()
                    ).orElse(null);
                    if (cookie == null) {
                        extractedErrorResponse(response);
                        return 0;
                    } else {
                        if(checkTokenInterceptor.checkToken(cookie.getValue())){
                            return 1;
                        }else {
                            extractedErrorResponse(response);
                            return 0;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("  ===> checkOssToken error {}", e.getMessage());
        }
        return null;
    }


    /**
     *  给 response 添加错误信息
     */
    private static void extractedErrorResponse(HttpServletResponse response) throws IOException {
        response.setHeader("content-type", "application/json;charset=UTF-8");
        response.getOutputStream().write(JSON.toJSONString(ExceptionResultWrap
                .result(TokenExceptionCode.TOKEN_ERROR.getCode(), TokenExceptionCode.TOKEN_ERROR.getMessage())).getBytes(StandardCharsets.UTF_8));
    }


}
