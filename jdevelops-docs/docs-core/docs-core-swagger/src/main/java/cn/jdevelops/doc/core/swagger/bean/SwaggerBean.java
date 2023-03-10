package cn.jdevelops.doc.core.swagger.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;

import java.util.Objects;

/**
 * swagger配置类
 * @author tn
 * @version 1
 * @date 2020/6/19 9:56
 */
@ConfigurationProperties(prefix = "jdevelops.swagger")
@Component
@Getter
@Setter
@ToString
public class SwaggerBean {

    /**
     * controller接口所在的包(多个包以分号拼接，eg. package.api;package.controller
     * ps: 注意如果包名前缀一样只需要写一个就行 eg. package.api1;package.api2 ＝ package.api
     */
    private String basePackage="cn.jdevelops.controller";


    /**
     * 网页标题
     */
    private String title="JdevelopsAPIs";

    /**
     * 当前文档的详细描述
     */
    private String description="详细描述";

    /**
     * 当前文档的版本
     */
    private String version="2.0.7";


    /**
     *  联络 url
     */
    private String contactUrl="https://tannn.cn/";

    /**
     *  联络人-作者
     */
    private String contactName="tan";

    /**
     *  联络邮箱
     */
    private String contactEmail="1445763190@qq.com";

    /**
     * swagger Docket  :
     *     SWAGGER_2
     *     SWAGGER_12
     *     OAS_30 (默认)
     */
    private String docket = "OAS_30";

    /**
     *  true 显示swagger， false 关闭swagger
     */
    private Boolean show=true;

    /**
     *  分组
     */
    private String groupName;

    /**
     *  是否添加全局Header token参数
     */
    private Boolean addHeaderToken = true;


    public DocumentationType getDocket() {
        switch (docket){
            case "SWAGGER_12":
                return DocumentationType.SWAGGER_12;
            case "SWAGGER_2":
                return DocumentationType.SWAGGER_2;
            default:
                return DocumentationType.OAS_30;
        }
    }

    public String getGroupName() {
        return Objects.isNull(groupName)?"Jdevelops接口":groupName;
    }
}
