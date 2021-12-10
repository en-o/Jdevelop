package cn.jdevelops.jpa.server.dao;

import cn.jdevelops.jap.core.util.CommUtils;
import cn.jdevelops.jpa.server.enums.FieldName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 公共dao层
 * <br></>
 * 默认了 两个方法  deleteByIdIn（删除），updateEntity（更新）
 * <br></>
 * {NoRepositoryBean：Spring Data Jpa在启动时就不会去实例化BaseRepository这个接口}
 * <br></>
 * @author tn
 * JpaSpecificationExecutor ：JPA复杂查询
 * JpaRepository 普通查询
 * @date 2020/5/14 15:31
 */
@NoRepositoryBean
public interface JpaBasicsDao<T, D> extends JpaRepository<T, D>, JpaSpecificationExecutor<T> {

    /**
     * 根据id删除批量删除
     *
     * @param ids id们
     * @return int
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    int deleteByIdIn(List<D> ids);


    /**
     * 根据id查询
     * @param id id
     * @return 自动扫描
     */
    List<T> findByIdIn(List<D> id);


     /**
     * 更新 根据id
     *
     * @param t           实体类型的 数据
     * @return Boolean
      */
    default Boolean updateEntity(T t) throws Exception {
        /* 跟根据ID获取需要更新的数据的 原始数据 */
        T  oidCamera = findById((D) CommUtils.getFieldValueByName(FieldName.ID.getFieldName(), t))
                .orElse(null);
        /**
         *将新数据中非空字段 克隆到原始数据中 实现更新
         * <p> oidCamera.copy(scCameraEntity); </p>
         */
        /** 获取method对象，其中包含方法名称和参数列表*/
        Method setName = oidCamera.getClass().getMethod("copy", Object.class);
        /** 执行method，t为实例对象，后面是方法参数列表；setName没有返回值 */
        setName.invoke(oidCamera, t);
        /** 保存克隆之后的数据  且 saveAndFlush立即生效 */
        T save = saveAndFlush(oidCamera);
        return true;
    }



}