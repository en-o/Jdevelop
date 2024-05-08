package cn.tannn.jdevelops.result.utils;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.validation.beanvalidation.LocaleContextMessageInterpolator;

import javax.validation.*;
import javax.validation.bootstrap.GenericBootstrap;
import java.util.Set;

/**
 * 测试 validator 的基类
 */
public class BeastValidatedTest {

    private Validator validator;

    @BeforeEach
    public void init() {
        GenericBootstrap bootstrap = Validation.byDefaultProvider();
        Configuration<?> configure = bootstrap.configure();
        MessageInterpolator targetInterpolator = configure.getDefaultMessageInterpolator();
        configure.messageInterpolator(new LocaleContextMessageInterpolator(targetInterpolator));
        ValidatorFactory validatorFactory = configure.buildValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    protected <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        return validator.validate(object, groups);
    }
}
