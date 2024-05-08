package cn.jdevelops.redis.core.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 登录限制配置
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-12 08:25
 */
@ConfigurationProperties(prefix = "jdevelops.login.limit")
@AutoConfiguration
public class LoginLimitConfig {

    /**
     * 过期时间
     * <pr>
     *     默认  600*1000(10分钟) 单位 毫秒
     * </pr>
     */
    private long expireTime = 600000;

    /**
     * 密码错误次数
     * <pr>
     *     默认5
     * </pr>
     */
    private Integer limit  = 5;

    @Override
    public String toString() {
        return "LoginLimitConfig{" +
                "expireTime=" + expireTime +
                ", limit=" + limit +
                '}';
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
