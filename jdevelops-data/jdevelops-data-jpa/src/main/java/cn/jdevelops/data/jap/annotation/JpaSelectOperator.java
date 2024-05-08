package cn.jdevelops.data.jap.annotation;


import cn.jdevelops.data.jap.enums.SQLConnect;
import cn.jdevelops.data.jap.enums.SQLOperator;
import cn.jdevelops.data.jap.enums.SQLOperatorWrapper;
import cn.jdevelops.data.jap.enums.SpecBuilderDateFun;

import java.lang.annotation.*;

/**
 * jpa查询条件
 *
 * @author tn
 * @version 1
 * @date 2021-12-08 15:21
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JpaSelectOperator {

    /**
     * sql 运算符 (JpaUtils方法用)
     * <pre>
     * 作用于 CommUtils.getSelectBean
     *  根据注解内容进行条件拼接，例如： 用的EQ 则： where 字段 = 值
     * </pre>
     *
     */
    SQLOperator operator() default SQLOperator.EQ;


    /**
     * sql 运算符 (Specifications方法用)
     * <pre>
     *  根据注解内容进行条件拼接，例如： 用的EQ 则： where 字段 = 值
     * </pre>
     *
     */
    SQLOperatorWrapper operatorWrapper() default SQLOperatorWrapper.EQ;


    /**
     * 空值验证 <br/>
     *
     * true: 空值不作为查询参数 <br/>
     * false: 需要查询为空的数据
     */
    boolean ignoreNull() default true;

    /**
     * ignoreNull = true 有效  <br/>
     * true: 不允许为 [null,""," "]  <br/>
     * false: 不允许为 null
     */
    boolean ignoreNullEnhance() default true;

    /**
     * 自定义查询用的字段名
     * <pr>
     *     1. 空时默认使用属性字段
     *     2. 如果是级联请用，级联对象名加其属性名组合（e.g 有个级联对象 Address address , 这里就用address.no根据其no查询）
     * </pr>
     *
     */
    String fieldName() default "";


    /**
     * 数据库字段是时间格式，实体字段也是时间格式的时候，构建查询会出错，所以这里要用函数格式化下
     * 默认不用，要用的时候自己按照枚举选择需要使用的格式化条件
     */
    SpecBuilderDateFun function() default SpecBuilderDateFun.NULL;


    /**
     * sql 连接符 (JpaUtils方法用)
     * <pre>
     * 作用于 CommUtils.getSelectBean
     *  根据注解内容进行条件拼接，例如： 用的AND 则： where 字段 = 值 and 字段 = 值
     * </pre>
     *
     */
    SQLConnect connect() default SQLConnect.AND;
}
