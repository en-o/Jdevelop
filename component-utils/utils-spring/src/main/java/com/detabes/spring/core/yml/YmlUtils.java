package com.detabes.spring.core.yml;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Properties;

/**
 * yml 直接读取工具
 *
 * @author lxw
 * @date 2021/1/22 13:33
 */
public class YmlUtils {

	/**
	 * 获取yml文件中key的对应值
	 *
	 * @param fileName 需要读取的yml文件名，如  application.yml
	 * @param key      需要去读的键名称，如 spring.profiles.active
	 * @return java.lang.String
	 * @author lxw
	 * @date 2021/1/22 13:31
	 */
	public static String getValueYml(String fileName, String key) {

		return getPropertiesYml(fileName).getProperty(key);
	}

	/**
	 * 获取myl文件中的所有内容，通过getProperty(key)的方法取,如：getProperty("spring.profiles.active")
	 *
	 * @param fileName yml文件名称 ，如  application.yml
	 * @return java.util.Properties
	 * @author lxw
	 * @date 2021/1/22 13:49
	 */
	public static Properties getPropertiesYml(String fileName) {
		Resource resource = new ClassPathResource(fileName);
		YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
		yamlFactory.setResources(resource);
		return yamlFactory.getObject();
	}
}
