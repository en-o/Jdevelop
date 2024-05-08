package cn.jdevelops.authentication.jredis.service;

import cn.jdevelops.api.exception.exception.TokenException;
import cn.jdevelops.authentication.jredis.entity.only.StorageToken;
import cn.jdevelops.authentication.jredis.entity.sign.RedisSignEntity;
import cn.jdevelops.authentication.jwt.exception.ExpiredRedisException;
import cn.jdevelops.authentication.jwt.server.LoginService;
import cn.jdevelops.authentication.jwt.util.JwtWebUtil;
import cn.jdevelops.authentication.jwt.vo.TokenSign;
import cn.jdevelops.util.jwt.constant.SignState;
import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.entity.SignEntity;
import cn.jdevelops.util.jwt.exception.LoginException;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 登录工具
 *
 * @author tan
 */
@ConditionalOnMissingBean(LoginService.class)
public class RedisLoginService implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(RedisLoginService.class);

    @Resource
    private RedisToken redisToken;

    @Resource
    private RedisUserState redisUserState;

    @Resource
    private RedisUserRole redisUserRole;


    /**
     * 登录
     * @param  loginMeta 登录需要的元数据
     * @return 签名
     */
    public <T> TokenSign login(RedisSignEntity<T> loginMeta) {

        // 判断是否需要重复登录
        try {
            // 查询当前用户是否已经登录
            StorageToken loginUser = redisToken.verify(loginMeta.getSubject());
            // 用户存在登录，判断是否需要重新登录
            if (Boolean.FALSE.equals(loginMeta.getOnlyOnline())) {
                // 继续使用当前token
                logger.warn("开始登录 - 登录判断 - 当前用户在线 - 继续使用当前token");
                return  new TokenSign(loginUser.getToken(), SignState.renewal);
            }
            // 重新登录
        } catch (ExpiredRedisException | TokenException e) {
            logger.warn("开始登录 - 登录判断 - 当前用户不在线 - 执行登录流程");
        }catch (Exception e) {
            logger.warn("开始登录 - 登录判断 - 用户在线情况判断失败 - 执行登录流程");
        }

        // 登录 - 生成token
        try {
            // token
            String sign = JwtService.generateToken(loginMeta);
            // 预备登录信息给redis存储
            StorageToken build = StorageToken.builder()
                    .subject(loginMeta.getSubject())
                    .onlyOnline(loginMeta.getOnlyOnline())
                    .alwaysOnline(loginMeta.getAlwaysOnline())
                    .token(sign)
                    .build();
            // 存储 用户登录信息
            redisToken.storage(build);

            // todo 这个考虑提到前面去，每次触发都改动
            // state
            if (null != loginMeta.getUserState()) {
                redisUserState.storage(loginMeta.getUserState());
            }
            // role
            if (null != loginMeta.getUserRole()) {
                redisUserRole.storage(loginMeta.getUserRole());
            }
            // 返回token
            return new TokenSign(sign);
        } catch (JoseException e) {
            throw new LoginException("登录异常，请重新登录", e);
        }
    }


    @Override
    public <T, S extends SignEntity<T>> TokenSign login(S subject) {
        RedisSignEntity<T> redisSign = new RedisSignEntity<>(subject);
        return login(redisSign);
    }

    @Override
    public boolean isLogin(HttpServletRequest request) {
        return isLogin(request, false);
    }

    @Override
    public boolean isLogin(String subject) {
        try {
            redisToken.load(subject);
            return true;
        } catch (Exception e) {
            logger.warn("登录失效", e);
        }
        return false;
    }

    @Override
    public boolean isLogin(HttpServletRequest request, Boolean cookie) {
        try {
            String token = JwtWebUtil.getToken(request, cookie);
            redisToken.loadByToken(token);
            return true;
        } catch (Exception e) {
            logger.warn("登录失效", e);
        }
        return false;
    }

    @Override
    public void loginOut(HttpServletRequest request) {
        try {
            String subject = JwtWebUtil.getTokenSubject(request);
            redisToken.remove(subject);
            redisUserState.remove(subject);
            redisUserRole.remove(subject);
        } catch (Exception e) {
            logger.warn("退出失败", e);
        }
    }

    @Override
    public void loginOut(String subject) {
        try {
            redisToken.remove(subject);
            redisUserState.remove(subject);
            redisUserRole.remove(subject);
        } catch (Exception e) {
            logger.warn("退出失败", e);
        }
    }

    @Override
    public void loginOut(List<String> subject) {
        try {
            redisToken.remove(subject);
            redisUserState.remove(subject);
            redisUserRole.remove(subject);
        } catch (Exception e) {
            logger.warn("退出失败", e);
        }
    }
}
