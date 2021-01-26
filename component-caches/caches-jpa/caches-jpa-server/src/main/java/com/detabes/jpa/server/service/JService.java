package com.detabes.jpa.server.service;

import com.detabes.result.page.ResourcePage;
import com.detabes.result.response.PageVO;
import com.detabes.result.response.SortVO;

import java.util.List;

/**
 * jpa公共service
 *
 * @author tn
 * @className JService
 * @date 2021-01-22 13:35
 */
public interface JService<T> {

    /**
     * 保存数据 返回实体
     *
     * @param t 实体
     * @return T
     */
    T saveByBean(T t);

    /**
     * 保存数据 返回 boolean
     *
     * @param t t
     * @return Boolean
     */
    Boolean saveByBoolean(T t);

    /**
     * 根据ids删除对象
     *
     * @param ids ids
     * @return Boolean
     */
    Boolean deleteById(final List<Integer> ids);

    /**
     * 根据id删除对象
     *
     * @param id id
     * @return Boolean
     */
    Boolean deleteById(final Integer id);

    /**
     * 根据UuIds删除对象
     *
     * @param uuid UuIds
     * @return Boolean
     */
    Boolean deleteByUuid(final List<String> uuid);

    /**
     * 根据Uuid删除对象
     *
     * @param uuid Uuid
     * @return Boolean
     */
    Boolean deleteByUuid(final String uuid);

    /**
     * 更新数据 返回实体
     *
     * @param t 实体 id一定要有且键名为ID
     * @return Boolean
     * @throws Exception Exception
     */
    Boolean updateByBean(T t);

    /**
     * 根据 id 查询
     *
     * @param id id
     * @return T
     */
    T findById(final Integer id);

    /**
     * 根据 uuid 查询
     *
     * @param uuid uuid
     * @return T
     */
    T findByUuid(final String uuid);

    /**
     * 默认可以不用的实现
     * 根据 t 中的数据查询 and
     *
     * @param t t
     * @return List<T>
     */
    List<T> findByBean(T t);

    /**
     * 分页查询
     *
     * @param t      查询条件
     * @param pageVO 分页
     * @param sortVO 排序
     * @param clazz  返回实体类型
     * @param <R>    返回实体类型
     * @return ResourcePage<List < R>>
     */
    <R> ResourcePage<List<R>> findByBean(T t, PageVO pageVO, SortVO sortVO, Class<R> clazz);

}
