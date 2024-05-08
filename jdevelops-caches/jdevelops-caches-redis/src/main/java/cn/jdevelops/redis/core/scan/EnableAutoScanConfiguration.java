package cn.jdevelops.redis.core.scan;

import cn.jdevelops.redis.core.config.LoginLimitConfig;
import cn.jdevelops.redis.core.config.RedisConfig;
import cn.jdevelops.redis.core.config.RedisProxy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 *
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({
        RedisConfig.class,
        RedisProxy.class,
        LoginLimitConfig.class
})
public class EnableAutoScanConfiguration {

}
