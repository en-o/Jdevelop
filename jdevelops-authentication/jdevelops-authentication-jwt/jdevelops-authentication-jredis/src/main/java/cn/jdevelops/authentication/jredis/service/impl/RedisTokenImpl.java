package cn.jdevelops.authentication.jredis.service.impl;

import cn.jdevelops.authentication.jredis.constant.RedisJwtKey;
import cn.jdevelops.authentication.jredis.entity.only.StorageToken;
import cn.jdevelops.authentication.jredis.service.RedisToken;
import cn.jdevelops.authentication.jredis.util.RedisUtil;
import cn.jdevelops.authentication.jwt.exception.ExpiredRedisException;
import cn.jdevelops.util.jwt.config.JwtConfig;
import cn.jdevelops.util.jwt.core.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static cn.jdevelops.api.result.emums.TokenExceptionCode.REDIS_EXPIRED_USER;

/**
 * 用户token
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/12/4 9:45
 */
@ConditionalOnMissingBean(RedisToken.class)
public class RedisTokenImpl implements RedisToken {


    private static final Logger LOG = LoggerFactory.getLogger(RedisTokenImpl.class);

    /**
     * reids
     */
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private JwtConfig jwtConfig;


    @Override
    public void storage(StorageToken token) {
        String loginRedisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                , RedisJwtKey.REDIS_USER_LOGIN_FOLDER
                , token.getSubject());
        redisTemplate.boundHashOps(loginRedisFolder).put(token.getSubject(),
                token);
        if (Boolean.TRUE.equals(token.getAlwaysOnline())) {
            // 永不过期
            redisTemplate.persist(loginRedisFolder);
        } else {
            // 设置过期时间（ 小时
            redisTemplate.expire(loginRedisFolder, jwtConfig.getLoginExpireTime(), TimeUnit.HOURS);
        }
    }

    @Override
    public void refresh(String subject) {
        String loginRedisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                ,RedisJwtKey.REDIS_USER_LOGIN_FOLDER, subject);
        Object loginRedis = redisTemplate.boundHashOps(loginRedisFolder).get(subject);
        if (Objects.isNull(loginRedis)) {
            LOG.warn("{}用户未登录，不需要刷新", subject);
        } else {
            StorageToken tokenRedis = (StorageToken) loginRedis;
            if (Boolean.TRUE.equals(tokenRedis.getAlwaysOnline())) {
                LOG.warn("{}用户是永久在线用户，不需要刷新", subject);
            } else {
                // 设置过期时间（毫秒
                redisTemplate.expire(loginRedisFolder, jwtConfig.getLoginExpireTime(), TimeUnit.HOURS);
            }
        }
    }

    @Override
    public void refreshByToken(String token) {
        String subjectExpires = JwtService.getSubjectExpires(token);
        refresh(subjectExpires);
    }

    @Override
    public void remove(String subject) {
        try {
            String redisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                    ,RedisJwtKey.REDIS_USER_LOGIN_FOLDER, subject);
            redisTemplate.delete(redisFolder);
        } catch (Exception e) {
            LOG.error("删除" + subject + " <==> token失败", e);
        }
    }

    @Override
    public void removeByToken(String token) {
        String subjectExpires = JwtService.getSubjectExpires(token);
        remove(subjectExpires);
    }

    @Override
    public void remove(List<String> subject) {
        try {
            Set<String> keys = new HashSet<>();
            for (String key : subject) {
                String redisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                        ,RedisJwtKey.REDIS_USER_LOGIN_FOLDER, key);
                keys.add(redisFolder);
            }
            if (!keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            LOG.error("删除tokens失败", e);
        }
    }

    @Override
    public void removeByToken(List<String> token) {
        for (String tokenRm : token) {
            String subjectExpires = JwtService.getSubjectExpires(tokenRm);
            removeByToken(subjectExpires);
        }
    }

    @Override
    public StorageToken load(String subject) {
        String loginRedisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                ,RedisJwtKey.REDIS_USER_LOGIN_FOLDER, subject);
        Object loginRedis = redisTemplate.boundHashOps(loginRedisFolder).get(subject);
        if (Objects.isNull(loginRedis)) {
            throw new ExpiredRedisException(REDIS_EXPIRED_USER);
        } else {
            return (StorageToken) loginRedis;
        }
    }

    @Override
    public StorageToken loadByToken(String token) {
        String subjectExpires = JwtService.getSubjectExpires(token);
        return load(subjectExpires);
    }

    @Override
    public StorageToken verify(String subject) {
        String loginRedisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                ,RedisJwtKey.REDIS_USER_LOGIN_FOLDER, subject);
        StorageToken tokenRedis;
        Object loginRedis = redisTemplate.boundHashOps(loginRedisFolder).get(subject);
        if (Objects.isNull(loginRedis)) {
            throw new ExpiredRedisException(REDIS_EXPIRED_USER);
        } else {
            tokenRedis = (StorageToken) loginRedis;
        }
        return tokenRedis;
    }

    @Override
    public StorageToken verifyByToken(String token) {
        String subjectExpires = JwtService.getSubjectExpires(token);
        return verify(subjectExpires);
    }
}
