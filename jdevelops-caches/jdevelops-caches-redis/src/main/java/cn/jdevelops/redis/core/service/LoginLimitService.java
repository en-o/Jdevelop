package cn.jdevelops.redis.core.service;

import cn.jdevelops.api.exception.exception.LoginLimitException;
import cn.jdevelops.redis.core.config.LoginLimitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.jdevelops.api.result.emums.UserException.LOGIN_LIMIT;

/**
 * 登录限制
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-12 09:24
 */
@ConditionalOnMissingBean(LoginLimitService.class)
public class LoginLimitService {
    private static final Logger LOG = LoggerFactory.getLogger(LoginLimitService.class);

    /**
     * redis存储的key名前缀
     */
    static final String REDIS_USER_LIMIT_FOLDER = "user_limit";
    /**
     * reids
     */
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private final LoginLimitConfig loginLimitConfig;


    public LoginLimitService(LoginLimitConfig loginLimitConfig) {
        this.loginLimitConfig = loginLimitConfig;
    }

    /**
     * 错误验证（放在登录接口第一行）
     * @param username 登录名
     * @param responseStatus 是否修改 http请求的status， 默认false都是200, true=403
     */
    public void verify(String username, boolean responseStatus) {
        LOG.debug("===> verify login error sum ...");
        String redisFolder = getRedisFolder(username);
        Object loginLimit = redisTemplate.boundHashOps(redisFolder).get(username);
        if (Objects.nonNull(loginLimit)) {
            int loginLimitInt = (int) loginLimit;
            Integer limit = loginLimitConfig.getLimit();
            if(loginLimitInt >= limit){
                LOG.debug("===> login  error  login sum {}, limit sum {}", loginLimit, limit);
                throw new LoginLimitException(LOGIN_LIMIT).setHttpServletResponseStatus(responseStatus);
            }
        }
    }



    /**
     * 错误记录 （放在错误调用里）
     *
     * @param username 登录名
     */
    public void limit(String username) {
        String redisFolder = getRedisFolder(username);
        Object loginLimit = redisTemplate.boundHashOps(redisFolder).get(username);
        if (Objects.isNull(loginLimit)) {
            redisTemplate.boundHashOps(redisFolder).put(username,
                    1);
        } else {
            int loginLimitInt = (int) loginLimit;
            redisTemplate.boundHashOps(redisFolder).put(username,
                    loginLimitInt + 1);
        }
        LOG.debug("===> record login error  user: {} ", username);
        // 设置过期时间（秒
        redisTemplate.expire(redisFolder, loginLimitConfig.getExpireTime(), TimeUnit.MILLISECONDS);
    }


    /**
     * redis简单存储建立文件夹
     *
     * @param username 登录名
     * @return folderName:key
     */
    private static String getRedisFolder(String username) {
        return REDIS_USER_LIMIT_FOLDER + ":" + username;
    }

}
