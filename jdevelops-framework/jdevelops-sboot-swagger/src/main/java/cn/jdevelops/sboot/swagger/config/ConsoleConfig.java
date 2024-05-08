package cn.jdevelops.sboot.swagger.config;

import cn.jdevelops.sboot.swagger.domain.SwaggerProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.annotation.Resource;

import static cn.jdevelops.sboot.swagger.core.constant.PublicConstant.COLON;
import static cn.jdevelops.sboot.swagger.core.constant.PublicConstant.SPIRIT;
import static cn.jdevelops.sboot.swagger.core.util.SwaggerUtil.getRealIp;

/**
 * 控制台输出
 *
 * @author tn
 * @version 1
 * @date 2023-03-12 18:54:25
 */
@ConditionalOnProperty(name = "jdevelops.swagger.console.enabled", havingValue = "true", matchIfMissing = true)
public class ConsoleConfig implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory.getLogger(ConsoleConfig.class);

	@Value("${server.port:8080}")
	private int serverPort;

	@Value("${server.servlet.context-path:/}")
	private String serverName;


	@Resource
	private SwaggerProperties swaggerBean;


	@Override
	public void run(ApplicationArguments args) {
		try {
			if (SPIRIT.equals(serverName)) {
				serverName = "";
			}
			String groupStr = "";
			String groupName = swaggerBean.getGroupName();
			if (StringUtils.isNotBlank(groupName)) {
				groupStr = "?group=" + swaggerBean.getGroupName();
			}
			LOG.info("\n----------------------------------------------------------\n\t" +
					" swagger 启动. Access URLs:\n\t" +
					"swagger 启动成功！接口文档地址(cloud没有页面)-HTML: (http://" + getRealIp() + COLON + serverPort + serverName + "/doc.html" + ")\n\t" +
					"swagger 启动成功！接口文档地址-JSON: (http://" + getRealIp() + COLON + serverPort + serverName + "/v2/api-docs" + groupStr + ")\n\t" +
					"swagger 启动成功！接口文档地址-OpenApi-JSON: (http://" + getRealIp() + COLON + serverPort + serverName + "/v3/api-docs" + groupStr + ")\n\t" +
					"----------------------------------------------------------");


		} catch (Exception ignored) {
		}
	}




}

