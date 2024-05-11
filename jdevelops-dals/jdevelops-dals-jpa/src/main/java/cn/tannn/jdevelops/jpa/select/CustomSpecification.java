package cn.tannn.jdevelops.jpa.select;

import cn.hutool.core.util.ReflectUtil;
import cn.tannn.jdevelops.annotations.jpa.JpaSelectIgnoreField;
import cn.tannn.jdevelops.annotations.jpa.JpaSelectOperator;
import cn.tannn.jdevelops.annotations.jpa.enums.SQLConnect;
import cn.tannn.jdevelops.jpa.select.criteria.ExtendSpecification;
import cn.tannn.jdevelops.jpa.select.criteria.Restrictions;
import cn.tannn.jdevelops.jpa.select.criteria.SimpleExpression;
import cn.tannn.jdevelops.jpa.utils.IObjects;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 自定义 建议用 {@link ExtendSpecification}
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/11 下午3:54
 */
@Deprecated
public class CustomSpecification {
    /**
     * 组装查询条件
     *
     * @param bean 条件实体
     * @param <T>  返回实体
     * @param <B>  条件实体
     * @return JPAUtilExpandCriteria
     */
    private static <T, B> ExtendSpecification<T> selectWheres(B bean) {
        ExtendSpecification<T> jpaSelect = new ExtendSpecification<>();
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        for (int i = 0, fieldsLength = fields.length; i < fieldsLength; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if ("serialVersionUID".equals(fieldName)) {
                continue;
            }
            // 字段被忽略
            JpaSelectIgnoreField ignoreField = field.getAnnotation(JpaSelectIgnoreField.class);
            if (Objects.nonNull(ignoreField)) {
                continue;
            }
            JpaSelectOperator selectOperator = field.getAnnotation(JpaSelectOperator.class);
            Object fieldValue = ReflectUtil.getFieldValue(bean, field);
            if (Objects.nonNull(selectOperator)) {
                // 使用自定义的名字
                if (!IObjects.isBlank(selectOperator.fieldName())) {
                    fieldName = selectOperator.fieldName();
                }
                SimpleExpression simpleExpression = jpaSelectOperatorSwitch(selectOperator, fieldName, fieldValue);
                if (Objects.equals(selectOperator.connect(), SQLConnect.OR)) {
                    jpaSelect.or(simpleExpression);
                } else {
                    jpaSelect.add(simpleExpression);
                }
            } else {
                // 没有注解所有属性都要处理成条件
                jpaSelect.add(Restrictions.eq(fieldName, fieldValue, true, true));
            }
        }
        return jpaSelect;
    }


    /**
     * 根据注解组装  jpa动态查询
     *
     * @param annotation JpaSelectOperator 注解
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @return SimpleExpression
     */
    public static SimpleExpression jpaSelectOperatorSwitch(JpaSelectOperator annotation,
                                                           String fieldName,
                                                           Object fieldValue) {
        switch (annotation.operator()) {
            case NE:
                return Restrictions.ne(fieldName, fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance(), annotation.function());
            case LIKE:
                return Restrictions.like(fieldName, fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance(),  annotation.function());
            case NOTLIKE:
                return Restrictions.notLike(fieldName, fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance(),  annotation.function());
            case LLIKE:
                return Restrictions.llike(fieldName, fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance(),  annotation.function());
            case RLIKE:
                return Restrictions.rlike(fieldName, fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance(),  annotation.function());
            case LT:
                return Restrictions.lt(fieldName, fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance(),  annotation.function());
            case GT:
                return Restrictions.gt(fieldName, fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance(),  annotation.function());
            case LTE:
                return Restrictions.lte(fieldName, fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance(),  annotation.function());
            case GTE:
                return Restrictions.gte(fieldName, fieldValue, annotation.ignoreNull(), annotation.ignoreNullEnhance(),  annotation.function());
            case ISNULL:
                return Restrictions.isNull(fieldName, annotation.function());
            case ISNOTNULL:
                return Restrictions.isNotNull(fieldName, annotation.function());
            case BETWEEN:
                // 值以逗号隔开
                return Restrictions.between(fieldName, fieldValue.toString().trim(), annotation.ignoreNull(), annotation.ignoreNullEnhance(),  annotation.function());
            case EQ:
            default:
                return Restrictions.eq(fieldName, fieldValue, annotation.ignoreNull(),  annotation.ignoreNullEnhance(), annotation.function());
        }
    }
}