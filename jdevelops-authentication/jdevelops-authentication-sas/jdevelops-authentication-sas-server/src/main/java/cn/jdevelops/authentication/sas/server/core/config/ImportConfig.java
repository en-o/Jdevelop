package cn.jdevelops.authentication.sas.server.core.config;

import cn.jdevelops.authentication.sas.server.oauth.model.oidc.CustomOidcUserInfoService;
import cn.jdevelops.authentication.sas.server.user.service.AuthenticationService;
import cn.jdevelops.authentication.sas.server.user.service.JUserDetailsService;
import cn.jdevelops.authentication.sas.server.user.service.impl.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 导入
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/1/25 10:01
 */
@AutoConfiguration
//@EnableJpaRepositories(basePackages = "cn.jdevelops.authentication.sas.server.oauth.dao")
//@EntityScan("cn.jdevelops.authentication.sas.server.oauth.entity")
public class ImportConfig {

    @Bean
    @ConditionalOnMissingBean(AuthenticationService.class)
    public AuthenticationService authenticationService(){
        return new AuthenticationService(){};
    }

    @Bean
    @ConditionalOnMissingBean(JUserDetailsService.class)
    public JUserDetailsService jUserDetailsService(AuthenticationService authenticationService){
        return new UserDetailsServiceImpl(authenticationService);
    }

    @Bean
    @ConditionalOnMissingBean(CustomOidcUserInfoService.class)
    public CustomOidcUserInfoService customOidcUserInfoService(AuthenticationService authenticationService){
        return new CustomOidcUserInfoService(authenticationService);
    }




}
