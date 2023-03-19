package cn.jdevelops.sboot.jpa.config;

import cn.jdevelops.schema.LocalDataSourceLoader;
import cn.jdevelops.schema.properties.DataBaseProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置
 * @author tn
 * @date 2021-08-02 09:00
 */
@Configuration
public class JpaConfiguration {


    /**
     * 建库
     */
    @Bean
    @ConditionalOnMissingBean(value = DataBaseProperties.class)
    public DataBaseProperties dataBaseProperties() {
        return new DataBaseProperties();
    }


    /**
     * 建库
     */
    @Bean
    @ConditionalOnMissingBean(value = LocalDataSourceLoader.class)
    public LocalDataSourceLoader localDataSourceLoader() {
        return new LocalDataSourceLoader();
    }
}