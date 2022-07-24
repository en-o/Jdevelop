package cn.jdevelops.jwt.annotation;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

/**
 * 不刷新token
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-24 03:34
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NotRefreshToken {
}
