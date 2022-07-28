package cn.jdevelops.jredis.service;


import cn.jdevelops.jredis.entity.only.StorageUserTokenEntity;
import cn.jdevelops.jredis.entity.RedisAccount;
import cn.jdevelops.jredis.exception.ExpiredRedisException;

import java.util.List;

/**
 * redis
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-22 14:06
 */
public interface RedisService {

    /**
     * 存放 用户TOKEN
     *
     * @param storageUserTokenEntity 存储用户登录token
     */
    void storageUserToken(StorageUserTokenEntity storageUserTokenEntity);

    /**
     * 刷新用户token
     *
     * @param subject 用户唯一值(一般用用户的登录名
     */
    void refreshUserToken(String subject);


    /**
     * 删除 用户TOKEN
     *
     * @param subject 用户唯一值(一般用用户的登录名
     */
    void removeUserToken(String subject);


    /**
     * 验证 用户TOKEN是否存在，存在则返回 token
     * 不存在，或者 token 异常就报错
     *
     * @param subject 用户唯一值(一般用用户的登录名
     * @return LoginTokenRedis
     * @throws ExpiredRedisException redis异常
     */
    StorageUserTokenEntity verifyUserTokenBySubject(String subject) throws ExpiredRedisException;

    /**
     * 验证 用户TOKEN是否存在，存在则返回 token
     * 不存在，或者 token 异常就报错
     *
     * @param token toekn
     * @return LoginTokenRedis
     * @throws ExpiredRedisException redis异常
     */
    StorageUserTokenEntity verifyUserTokenByToken(String token) throws ExpiredRedisException;


    /**
     * 获取存储的用户token详情
     * @param subject 用户唯一值(一般用用户的登录名
     * @return LoginTokenRedis
     */
    StorageUserTokenEntity loadUserTokenInfoBySubject(String subject) ;

    /**
     * 获取存储的用户token详情
     * @param token token
     * @return LoginTokenRedis
     */
    StorageUserTokenEntity loadUserTokenInfoByToken(String token) ;


    /**
     * 验证用户的状态
     * 有问题则异常 (ExpiredRedisException)
     *
     * @param subject 用户唯一值(一般用用户的登录名
     * @throws ExpiredRedisException redis异常
     */
    void verifyUserStatus(String subject) throws ExpiredRedisException;


    /**
     * 存储 用户的状态
     *
     * @param account 用户
     */
    <T> void storageUserStatus(RedisAccount<T> account);

    /**
     * 加载用户的状态
     * 有问题则异常
     *
     * @param subject 用户唯一值(一般用用户的登录名
     * @return RedisAccount
     */
    <T> RedisAccount<T> loadUserStatus(String subject);


    /**
     * 存放 用户角色
     *
     * @param subject 用户唯一值(一般用用户的登录名
     * @param roles    权限集合
     */
    void storageUserRole(String subject, List<String> roles);


    /**
     * 加载用户角色
     *
     * @param subject 用户唯一值(一般用用户的登录名
     * @return List (如返回空对象则表示redis中无数据请先自行添加
     */
    List<String> loadUserRole(String subject);

}
