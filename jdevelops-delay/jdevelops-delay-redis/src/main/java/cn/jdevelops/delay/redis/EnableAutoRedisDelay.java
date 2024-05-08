package cn.jdevelops.delay.redis;


import cn.jdevelops.delay.core.entity.DelayQueueMessage;
import cn.jdevelops.delay.core.service.DelayService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;


/**
 * 注入配置
 * @author tnnn
 */
@AutoConfiguration
public  class EnableAutoRedisDelay {

    /**
     * 加载lua脚本
     * @return DefaultRedisScript
     */
    @ConditionalOnMissingBean(DefaultRedisScript.class)
    @Bean
    public DefaultRedisScript<List> delayRedisScript() {
        DefaultRedisScript<List> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setResultType(List.class);
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/delay.lua")));
        return defaultRedisScript;
    }

    /**
     * 得到消息之后的执行动作
     * @return DelayService
     */
    @ConditionalOnMissingBean(DelayService.class)
    @Bean
    public DelayService<DelayQueueMessage> delayService() {
        return new RedisDelayService<>(DelayQueueMessage.class);
    }

}
