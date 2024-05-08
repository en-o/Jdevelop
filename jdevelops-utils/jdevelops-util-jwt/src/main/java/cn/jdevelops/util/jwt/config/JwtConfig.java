package cn.jdevelops.util.jwt.config;

import cn.jdevelops.util.jwt.constant.JwtConstant;
import cn.jdevelops.util.jwt.entity.JCookie;
import cn.jdevelops.util.jwt.entity.OssLocalAuthentication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * jwt秘钥跟过期时间
 * @author tn
 * @version 1
 * @date 2020/6/19 11:23
 */

@ConfigurationProperties(prefix = "jdevelops.jwt")
@Component
public class JwtConfig {

    /**
     * token私钥
     * ps: hmacsha256(你的密钥)[<a href="http://www.jsons.cn/allencrypt/">...</a>]
     */
    private String tokenSecret;


    /**
     * jwt token时间
     * 默认  过期时间为一天 (单位:小时)
     */
    private long expireTime;


    /**
     * token redis 时间
     * 默认  过期时间为一天 (单位:小时)
     *
     */
    private long loginExpireTime;

    /**
     *  是否开启从cookie中获取token(顺序为： Header -> Parameter -> Cookies)
     *  默认false
     */
    @NestedConfigurationProperty
    private JCookie cookie;

    /**
     * token参数的key自定义
     * 默认 token
     */
    private String tokenName;

    /**
     * 发行人;无值有默认
     */
    private String issuer;

    /**
     * 全局刷新token的过期时间。默认true:刷新，false:不刷新
     */
    private Boolean callRefreshToken;

    /**
     * 是否开启  ApiPermission 注解 [默认false]
     */
    private Boolean verifyPermission;

    /**
     * 是否开启  ApiPlatform 注解 [默认false]
     */
    private Boolean verifyPlatform;


    /**
     * 本地文件上次，文件访问接口鉴权
     */
    @NestedConfigurationProperty
    private OssLocalAuthentication oss;


    /**
     * 登录相关的jwt存储前缀，区分不同项目 prefix:RedisJwtKey.class:xx:xx
     */
    private String prefix;

    public String getTokenSecret() {
        if(Objects.isNull(tokenSecret)||tokenSecret.length()<=0){
            return "b30715ff9b4d60c4dff8044acfb33ba091544b2e21825672edc38799f52f1895";
        }
        return tokenSecret;
    }

    public JCookie getCookie() {
        if(Objects.isNull(cookie)){
            return new JCookie(false, JwtConstant.TOKEN);
        }
        return cookie;
    }

    public String getTokenName() {
        if(Objects.isNull(tokenName)){
            return JwtConstant.TOKEN;
        }
        return tokenName;
    }



    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public long getExpireTime() {
        if(loginExpireTime<=0){
            return 24;
        }else {
            return expireTime;
        }
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getLoginExpireTime() {
        if(loginExpireTime<=0){
            return 24;
        }else {
            return loginExpireTime;
        }
    }

    public void setLoginExpireTime(long loginExpireTime) {
        this.loginExpireTime = loginExpireTime;
    }

    public void setCookie(JCookie cookie) {
        this.cookie = cookie;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getIssuer() {
        if(Objects.isNull(issuer)){
            return "jdevelops";
        }
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }


    public Boolean getCallRefreshToken() {
        if(Objects.isNull(callRefreshToken)){
            return true;
        }
        return callRefreshToken;
    }

    public void setCallRefreshToken(Boolean callRefreshToken) {
        this.callRefreshToken = callRefreshToken;
    }

    public Boolean getVerifyPermission() {
        if(Objects.isNull(verifyPermission)){
            return false;
        }
        return verifyPermission;
    }

    public void setVerifyPermission(Boolean verifyPermission) {
        this.verifyPermission = verifyPermission;
    }

    public Boolean getVerifyPlatform() {
        if(Objects.isNull(verifyPlatform)){
            return false;
        }
        return verifyPlatform;
    }

    public void setVerifyPlatform(Boolean verifyPlatform) {
        this.verifyPlatform = verifyPlatform;
    }

    public OssLocalAuthentication getOss() {
        if(null == oss){
            return new OssLocalAuthentication();
        }
        return oss;
    }

    public void setOss(OssLocalAuthentication oss) {
        this.oss = oss;
    }


    public String getPrefix() {
        return prefix==null ? "project" : prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "JwtConfig{" +
                "tokenSecret='" + tokenSecret + '\'' +
                ", expireTime=" + expireTime +
                ", loginExpireTime=" + loginExpireTime +
                ", cookie=" + cookie +
                ", tokenName='" + tokenName + '\'' +
                ", issuer='" + issuer + '\'' +
                ", callRefreshToken=" + callRefreshToken +
                ", verifyPermission=" + verifyPermission +
                ", verifyPlatform=" + verifyPlatform +
                ", oss=" + oss +
                ", prefix='" + prefix + '\'' +
                '}';
    }
}
