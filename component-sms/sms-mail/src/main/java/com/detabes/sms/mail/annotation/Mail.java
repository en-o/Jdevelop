package com.detabes.sms.mail.annotation;

import com.detabes.sms.mail.config.MailConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lmz
 * @date 2021/3/29  9:27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(MailConfiguration.class)
public @interface Mail {
}
