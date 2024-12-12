package cn.tannn.jdevelops.jdectemplate.util;

import cn.tannn.jdevelops.jdectemplate.enums.SelectType;
import cn.tannn.jdevelops.result.request.Paging;
import cn.tannn.jdevelops.result.response.PageResult;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static cn.tannn.jdevelops.jdectemplate.util.JdbcUtils.insertFirstElement;

/**
 * 工具类 【InteriorJdbcTemplateUtil 这个的整理版本】
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/12/12 16:48
 */
public class JdbcTemplateUtil {

    /**
     * 查询
     * <p>
     * <> 如果sql自己组装好了参数，那args就可以不用传了， page除外必须传一个分页 </>
     *
     * @param jdbcTemplate jdbcTemplate
     * @param selectType   查询方式{@link SelectType}
     * @param bean         数据类型[也可以是基础类型]
     * @param sql          jdbcTemplate 风格的sql
     * @param args         sql里的参数，如果是参数{@link Paging}一定要放在第一个
     * @return Object
     */
    public static Object queryForObject(JdbcTemplate jdbcTemplate
            , SelectType selectType
            , Class<?> bean
            , String sql
            , Object... args) {

        if (JdbcUtils.isBasicType(bean)) {
            return switch (selectType) {
                case LIST -> jdbcTemplate.queryForList(sql, bean, args);
                case PAGE -> {
                    Paging paging = (Paging) JdbcUtils.removeFirstElement(args);
                    yield InteriorJdbcTemplateUtil.paging(jdbcTemplate, sql, bean, paging, args);
                }
                case MAP -> jdbcTemplate.queryForObject(sql, bean, args);
            };
        }
        return switch (selectType) {
            case LIST -> jdbcTemplate.query(sql, JdbcUtils.rowMapper(bean), args);
            case PAGE -> {
                Paging paging = (Paging) JdbcUtils.removeFirstElement(args);
                yield InteriorJdbcTemplateUtil.paging(jdbcTemplate, sql, bean, paging, args);
            }
            case MAP -> jdbcTemplate.queryForObject(sql, JdbcUtils.rowMapper(bean), args);
        };

    }


    /**
     * 查询
     * <p>
     * <> 如果sql自己组装好了参数，那args就可以不用传了， page除外必须传一个分页 </>
     *
     * @param jdbcTemplate jdbcTemplate
     * @param bean         数据类型
     * @param sql          jdbcTemplate 风格的sql
     * @param args         sql里的参数，如果是参数{@link Paging}一定要放在第一个
     * @return Object
     */
    public static <T> PageResult<T> queryForPage(JdbcTemplate jdbcTemplate
            , Class<?> bean
            , String sql
            , Object... args) {
        return (PageResult<T>) queryForObject(jdbcTemplate, SelectType.PAGE, bean, sql, args);
    }

    /**
     * 查询
     *
     * @param jdbcTemplate jdbcTemplate
     * @param bean         数据类型
     * @param sql          jdbcTemplate 风格的sql
     * @param args         sql里的参数
     * @return Object
     */
    public static <T> List<T> queryForList(JdbcTemplate jdbcTemplate
            , Class<?> bean
            , String sql
            , Object... args) {
        return (List<T>) queryForObject(jdbcTemplate, SelectType.LIST, bean, sql, args);
    }

    /**
     * 查询
     *
     * @param jdbcTemplate jdbcTemplate
     * @param bean         数据类型
     * @param sql          jdbcTemplate 风格的sql
     * @param args         sql里的参数
     * @return Object
     */
    public static <T> T queryForBean(JdbcTemplate jdbcTemplate
            , Class<?> bean
            , String sql
            , Object... args) {
        return (T) queryForObject(jdbcTemplate, SelectType.MAP, bean, sql, args);
    }

}
