package cn.jdevelops.api.result.emums;


/**
 * 公共枚举
 *
 * @author tn
 * @version V1.0
 * @date 2022-07-22 12:30:44
 **/

public class UserException {


    /**
     * 用户不存在
     */
    public static final ExceptionCode USER_EXIST_ERROR = new ExceptionCode(405, "账户或者密码错误，请检查后重试");



    /**
     * 用户密码错误
     */
    public static final ExceptionCode USER_PASSWORD_ERROR = new ExceptionCode(405, "账户或者密码错误，请检查后重试");




    /**
     * 账户已封禁
     */
    public static final ExceptionCode BANNED_ACCOUNT = new ExceptionCode(406, "账户已封禁");



    /**
     * 帐号被禁用帐号由于多次认证被禁用，请稍后再试
     */
    public static final ExceptionCode EXCESSIVE_ATTEMPTS_ACCOUNT = new ExceptionCode(407, "帐号被禁用帐号由于多次认证被禁用,请稍后再试");


    /**
     * 非白名单用户
     */
    public static final ExceptionCode WHITE_LIST_ACCOUNT = new ExceptionCode(408, "非白名单用户,请联系管理员");

    /**
     *  账号待审核,请联系管理员
     */
    public static final ExceptionCode AUDIT_ACCOUNT = new ExceptionCode(409, "账号待审核,请联系管理员");


    /**
     * 密码不一致，请重新输入
     */
    public static final ExceptionCode PWD_VERIFY = new ExceptionCode(410, "密码不一致，请重新输入");

    /**
     * 用户正在同步中，请稍后再试或者联系管理员
     */
    public static final ExceptionCode USER_SYNC_LOAING = new ExceptionCode(411, "用户正在同步中，请稍后再试或者联系管理员");


    /**
     * 账户已停用
     */
    public static final ExceptionCode DISABLED_ACCOUNT = new ExceptionCode(412, "账户已停用");

    /**
     * 频繁登录请稍后再试
     */
    public static final ExceptionCode LOGIN_LIMIT = new ExceptionCode(403, "频繁登录请稍后再试");

}
