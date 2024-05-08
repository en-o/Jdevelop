package cn.jdevelops.authentication.jredis.service.impl;

import cn.jdevelops.authentication.jredis.constant.RedisJwtKey;
import cn.jdevelops.authentication.jredis.entity.StorageUserState;
import cn.jdevelops.authentication.jredis.service.RedisUserState;
import cn.jdevelops.authentication.jredis.util.RedisUtil;
import cn.jdevelops.authentication.jwt.exception.DisabledAccountException;
import cn.jdevelops.util.jwt.config.JwtConfig;
import cn.jdevelops.util.jwt.constant.JwtMessageConstant;
import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.exception.LoginException;
import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static cn.jdevelops.api.result.emums.UserException.BANNED_ACCOUNT;
import static cn.jdevelops.api.result.emums.UserException.EXCESSIVE_ATTEMPTS_ACCOUNT;

/**
 * 用户状态
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/12/4 16:02
 */
@ConditionalOnMissingBean(RedisUserState.class)
public class RedisUserStateImpl implements RedisUserState {


    private static final Logger LOG = LoggerFactory.getLogger(RedisUserStateImpl.class);

    @Resource
    private JwtConfig jwtConfig;
    /**
     * reids
     */
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public void storage(StorageUserState state) {
        String userStateRedisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                ,RedisJwtKey.REDIS_USER_STATUS_FOLDER, state.getSubject());
        // 处理由于是泛型对象导致其他地方继承后有问题，
        String accountJson = JSON.toJSONString(state);
        redisTemplate.boundHashOps(userStateRedisFolder).put(state.getSubject(), accountJson);
        // 永不过期
        redisTemplate.persist(userStateRedisFolder);
    }

    @Override
    public void refresh(StorageUserState state) {
        storage(state);
    }

    @Override
    public StorageUserState load(String subject) {
        String userStateRedisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                ,RedisJwtKey.REDIS_USER_STATUS_FOLDER, subject);
        String redisUser;
        try {
            redisUser = (String) redisTemplate
                    .boundHashOps(userStateRedisFolder)
                    .get(subject);
        } catch (Exception e) {
            LOG.info("加载用户状态缓存失败");
            throw new LoginException(JwtMessageConstant.TOKEN_ERROR, e);
        }
        if(Objects.isNull(redisUser)){
            return null;
        }else {
            return JSON.to(StorageUserState.class, redisUser);
        }
    }

    @Override
    public void verify(String subject) {
        StorageUserState userState = load(subject);
        if (!Objects.isNull(userState)) {
            if (userState.isExcessiveAttempts()) {
                throw new DisabledAccountException(EXCESSIVE_ATTEMPTS_ACCOUNT);
            }
            if (userState.isDisabledAccount()) {
                throw new DisabledAccountException(BANNED_ACCOUNT);
            }
        }
    }

    @Override
    public void verifyByToken(String token) {
        String subjectExpires = JwtService.getSubjectExpires(token);
        verify(subjectExpires);
    }

    @Override
    public void remove(String subject) {
        try {
            String userStateRedisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                    ,RedisJwtKey.REDIS_USER_STATUS_FOLDER, subject);
            redisTemplate.delete(userStateRedisFolder);
        }catch (Exception e){
            LOG.error("删除"+subject+"userState失败", e);
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
                String userStateRedisFolder = RedisUtil.getRedisFolder(jwtConfig.getPrefix()
                        ,RedisJwtKey.REDIS_USER_STATUS_FOLDER, key);
                keys.add(userStateRedisFolder);
            }
            if(!keys.isEmpty()){
                redisTemplate.delete(keys);
            }
        }catch (Exception e){
            LOG.error("删除userStates失败", e);
        }
    }
}
