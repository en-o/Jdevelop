package cn.jdevelops.aop.api.log.enums;


import cn.jdevelops.aop.api.log.annotation.ApiLog;
import cn.jdevelops.aop.api.log.bean.ApiMonitoring;

/**
 * 操作日志的操作类型
 * {@link ApiLog}
 * @author tan
 */

public enum OperateTypeEnum {

    /**
     * 查询
     *
     */
    GET(1),
    /**
     * 新增
     */
    CREATE(2),
    /**
     * 修改
     */
    UPDATE(3),
    /**
     * 删除
     */
    DELETE(4),
    /**
     * 导出
     */
    EXPORT(5),
    /**
     * 导入
     */
    IMPORT(6),
    /**
     * 其它
     *
     * 在无法归类时，可以选择使用其它。因为还有操作名可以进一步标识
     */
    OTHER(0);

    /**
     * 类型
     */
    private final Integer type;


    public Integer getType() {
        return type;
    }

    OperateTypeEnum(Integer type) {
        this.type = type;
    }
}