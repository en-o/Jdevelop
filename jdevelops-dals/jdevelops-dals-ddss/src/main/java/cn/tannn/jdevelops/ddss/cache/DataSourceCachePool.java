package cn.tannn.jdevelops.ddss.cache;


import cn.tannn.jdevelops.ddss.config.DynamicDataSourceProperties;
import cn.tannn.jdevelops.ddss.exception.DynamicDataSourceException;
import cn.tannn.jdevelops.ddss.model.DynamicDatasourceEntity;
import cn.tannn.jdevelops.ddss.service.DynamicDatasourceService;
import cn.tannn.jdevelops.ddss.util.DynamicSpringBeanUtil;
import cn.tannn.jdevelops.ddss.util.ObjectUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据源缓存池
 *
 * @author tan
 */
public class DataSourceCachePool {

    private static final Logger log = LoggerFactory.getLogger(DataSourceCachePool.class);
    /**
     * 本地 数据源缓存
     */
    private static Map<String, HikariDataSource> dbSources = new HashMap<>();


    /**
     * 获取多数据源[DB中查询]
     *
     * @param dbName 数据源
     * @return 元数据
     */
    public static DynamicDatasourceEntity getDynamicDataSourceModelByDB(String dbName) {
        try {
            // 查询数据库中的 数据源元信息
            DynamicDatasourceService dynamicDatasourceService = DynamicSpringBeanUtil.getBean(DynamicDatasourceService.class);
//            DynamicDatasourceEntity dbSource = dynamicDatasourceService.findDyDatasourceEntity(dbName);
            // 查询有效的
            DynamicDatasourceEntity dbSource = dynamicDatasourceService.findEnable(dbName);
            if (dbSource != null) {
                // 存储的数据库账户密码是加密过后的，这里进行了解密处理
                DynamicDataSourceProperties properties = DynamicSpringBeanUtil.getBean(DynamicDataSourceProperties.class);
                String password = ObjectUtils.decryptAES(dbSource.getDatasourcePassword(), properties.getSalt());
                dbSource.setDatasourcePassword(password);
                String username = ObjectUtils.decryptAES(dbSource.getDatasourceUsername(), properties.getSalt());
                dbSource.setDatasourceUsername(username);
                return dbSource;
            } else {
                log.warn("数据源====《{}》 可能已被移除/无效，无法在对其进行操作", dbName);
                return null;
            }
        } catch (Exception e) {
            throw DynamicDataSourceException.specialMessage("数据源====《" + dbName + "》 可能已被移除无法在对其进行操作");
        }
    }

    public static HikariDataSource getCacheBasicDataSource(String dbName) {
        return dbSources.get(dbName);
    }

    /**
     * put 数据源缓存
     *
     * @param dbName 数据源名
     * @param db     HikariDataSource
     */
    public static void putCacheBasicDataSource(String dbName, HikariDataSource db) {
        dbSources.put(dbName, db);
    }

    /**
     * 清空数据源缓存
     */
    public static void cleanAllCache() {
        //关闭数据源连接
        for (Map.Entry<String, HikariDataSource> entry : dbSources.entrySet()) {
            HikariDataSource druidDataSource = entry.getValue();
            if (druidDataSource != null && druidDataSource.isClosed()) {
                druidDataSource.close();
            }
        }
        //清空缓存
        dbSources.clear();
    }

    public static void removeCache(String dbName) {
        //关闭数据源连接
        HikariDataSource dataSource = dbSources.get(dbName);
        if (dataSource != null) {
            dataSource.close();
        }
        //清空缓存
        dbSources.remove(dbName);
    }

}
