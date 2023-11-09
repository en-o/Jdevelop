package cn.jdevelops.sboot.authentication.jredis.service;

import cn.jdevelops.sboot.authentication.jredis.entity.base.BasicsAccount;
import cn.jdevelops.sboot.authentication.jwt.exception.ExpiredRedisException;

/**
 * 用户状态
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/11/9 16:20
 */
public interface RedisUserState {

    /**
     * 存储用户状态  [永不过期，改变时也要重置状态信息]
     *
     * @param state 用户状态
     */
    void storage(String state);

    /**
     * 刷新用户状态
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     */
    void refresh(String subject);

    /**
     * 查询用户状态
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     * @return 用户状态对象
     */
    String load(String subject);

    /**
     * 验证用户状态  [根据状态抛出相应的异常]
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     */
    void verify(String subject) ;

    /**
     * 验证用户状态  [根据状态抛出相应的异常]
     *
     * @param token token
     */
    void verifyByToken(String token) ;

}
