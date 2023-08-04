package cn.jdevelops.sboot.authentication.jredis.entity.sign;


import cn.jdevelops.util.jwt.entity.SignEntity;

import java.util.Objects;

/**
 * 用户登录签名办法token的数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 15:14
 */
public class RedisSignEntity<T> extends SignEntity<T> {

    /**
     * redis用 是否永久在线 (默认fales)
     */
    Boolean alwaysOnline;


    /**
     * redis用 唯一登录 ，以前的是否会被挤下线 （默认false：不挤）
     */
    Boolean onlyOnline;


    /**
     * 用户登录签名办法token的数据
     * @param subject 唯一
     * @param loginName 登录名
     * @param userId 用户id
     * @param userName 用户名
     * @param alwaysOnline token是否永久在线
     */
    public RedisSignEntity(String subject,
                           String loginName,
                           String userId,
                           String userName,
                           Boolean alwaysOnline,
                           Boolean onlyOnline) {
        super(subject,loginName,userId,userName);
        this.alwaysOnline = alwaysOnline;
        this.onlyOnline = onlyOnline;
    }

    public RedisSignEntity(SignEntity<T> subject) {
        super(subject.getSubject(),
                subject.getLoginName(),
                subject.getUserId(),
                subject.getUserName(),
                subject.getMap());
        this.alwaysOnline = false;
        this.onlyOnline = false;
    }

    public RedisSignEntity(SignEntity<T> subject, Boolean alwaysOnline, Boolean onlyOnline) {
        super(subject.getSubject(),
                subject.getLoginName(),
                subject.getUserId(),
                subject.getUserName(),
                subject.getMap());
        this.alwaysOnline = alwaysOnline;
        this.onlyOnline = onlyOnline;
    }


    @Override
    public String toString() {
        return "RedisSignEntity{" +
                "alwaysOnline=" + alwaysOnline +
                ", onlyOnline=" + onlyOnline +
                '}';
    }

    public Boolean getOnlyOnline() {
        if(Objects.isNull(onlyOnline)){
            return false;
        }
        return onlyOnline;
    }

    public void setOnlyOnline(Boolean onlyOnline) {
        this.onlyOnline = onlyOnline;
    }

    public Boolean getAlwaysOnline() {
        if(Objects.isNull(alwaysOnline)){
            return false;
        }
        return alwaysOnline;
    }

    public void setAlwaysOnline(Boolean alwaysOnline) {
        this.alwaysOnline = alwaysOnline;
    }

}
