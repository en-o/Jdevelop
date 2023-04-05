package cn.jdevelops.jwtweb.scan;

import cn.jdevelops.jwtweb.config.InterceptorBean;
import cn.jdevelops.jwtweb.config.WebApiConfig;
import cn.jdevelops.jwtweb.holder.ApplicationContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
public class EnableAutoScanConfiguration {

    @ConditionalOnMissingBean(ApplicationContextHolder.class)
    @Bean
    public ApplicationContextHolder applicationContextHolder(){
        return new ApplicationContextHolder();
    }
    @ConditionalOnMissingBean(InterceptorBean.class)
    @Bean
    public InterceptorBean interceptorBean(){
        return new InterceptorBean();
    }

    @ConditionalOnMissingBean(WebApiConfig.class)
    @Bean
    public WebApiConfig webApiConfig(){
        return new WebApiConfig();
    }
}
