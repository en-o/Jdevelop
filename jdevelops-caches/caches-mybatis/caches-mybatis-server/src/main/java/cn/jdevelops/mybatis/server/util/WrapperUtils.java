package cn.jdevelops.mybatis.server.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import cn.jdevelops.mybatis.server.vo.QueryVo;
import cn.jdevelops.result.response.SortVO;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author lmz
 * @date 2020/8/25  15:56
 */
public class WrapperUtils {
    public static <T> QueryWrapper<T> createWrapper(T t) {
        try {
            Map<String, Object> fieldList = ObjectUtil.getFieldList(t);
            Map<String, Object> params = new HashMap<>(16);
            QueryWrapper<T> queryWrapper = new QueryWrapper<>();
            fieldList.forEach((key, value) -> {
                if (value != null && StringUtils.isNotBlank(value.toString())) {
                    params.put(CamlCaseUtil.toLine(key), value);
                }
            });
            if (!params.isEmpty()) {
                queryWrapper = queryWrapper.allEq(params);
            }
            return queryWrapper;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * description: 条件添加排序
     *
     * @param wrapper wrapper
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<T>
     * @author lmz
     * @date 2020/12/16  11:34
     */
    public static <T> QueryWrapper<T> createOrderBy(QueryWrapper<T> wrapper, SortVO sortVO) {
        //判断排序方式 1是desc 0是asc
        if (sortVO!=null) {
            boolean flag = sortVO.getOrderDesc() == null || sortVO.getOrderDesc() != 1;
            wrapper = wrapper.orderBy(StringUtils.isNotBlank(sortVO.getOrderBy()), flag,
                    sortVO.getOrderBy());
        }
        return wrapper;
    }

    /**
     * description:
     *
     * @param queryVo queryVo
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<T>
     * @author lmz
     * @date 2020/8/26  16:09
     */
    public static <T> QueryWrapper<T> createWrapper(List<QueryVo> queryVo) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        for (QueryVo vo : queryVo) {
            switch (vo.getType()) {
                case "eq":
                    queryWrapper.eq(vo.getKey(), vo.getValue()); break;
                case "ne":
                    queryWrapper.ne(vo.getKey(), vo.getValue()); break;
                case "like":
                    queryWrapper.like(vo.getKey(), vo.getValue()); break;
                case "leftlike":
                    queryWrapper.likeLeft(vo.getKey(), vo.getValue()); break;
                case "rightlike":
                    queryWrapper.likeRight(vo.getKey(), vo.getValue()); break;
                case "notlike":
                    queryWrapper.notLike(vo.getKey(), vo.getValue()); break;
                case "gt":
                    queryWrapper.gt(vo.getKey(), vo.getValue()); break;
                case "lt":
                    queryWrapper.lt(vo.getKey(), vo.getValue()); break;
                case "ge":
                    queryWrapper.ge(vo.getKey(), vo.getValue()); break;
                case "le":
                    queryWrapper.le(vo.getKey(), vo.getValue()); break;
                default:
                    break;
            }

        }


        return queryWrapper;
    }
}