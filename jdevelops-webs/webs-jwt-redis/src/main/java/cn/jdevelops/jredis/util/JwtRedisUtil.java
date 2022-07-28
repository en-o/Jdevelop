package cn.jdevelops.jredis.util;

import cn.jdevelops.jredis.entity.LoginTokenRedis;
import cn.jdevelops.jredis.service.RedisService;
import cn.jdevelops.jwt.bean.JwtBean;
import cn.jdevelops.jwt.constant.JwtConstant;
import cn.jdevelops.jwt.util.ContextUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * 此项目常用工具
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-24 02:55
 */
public class JwtRedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtRedisUtil.class);

    /**
     * redis简单存储建立文件夹
     *
     * @param folderName 文件夹名
     * @param key        key
     * @return folderName:key
     */
    public static String getRedisFolder(String folderName, String key) {
        return folderName + ":" + key;
    }



    /**
     * 生成签名,并保存到redis中(默认有过期时间)
     *
     * @param subject      用户唯一凭证(一般是登录名
     * @param map          其余数据
     * @return 签名
     */
    public static String sign(String subject, Map<String, Object> map) {
        return sign(subject, map, false);
    }

    /**
     * 生成签名,并保存到redis中
     *
     * @param subject      用户唯一凭证(一般是登录名
     * @param map          其余数据
     * @param alwaysOnline 是否永久在线
     * @return 签名
     */
    public static String sign(String subject, Map<String, Object> map, boolean alwaysOnline) {
        JwtBean jwtBean = ContextUtil.getBean(JwtBean.class);
        RedisService redisService = ContextUtil.getBean(RedisService.class);
        //过期时间
        Date date = new Date(System.currentTimeMillis() + jwtBean.getExpireTime());
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
        //设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        // jwt
        JWTCreator.Builder builder = JWT.create();

        //jwt header
        builder.withHeader(header);
        // 用户自定义字段
        builder.withClaim(JwtConstant.TOKEN_KEY, subject);
        if (map != null) {
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                builder.withClaim(key, map.get(key) + "");
            }
        }
        // 签发时间
        builder.withIssuedAt(new Date());
        // 过期时间
        builder.withExpiresAt(date);
        // 发行人
        builder.withIssuer("jdevelops");
        // 主题
        builder.withSubject(subject);
        // 编号/版本
        builder.withJWTId(UUID.randomUUID().toString());
        // 生成token
        String sign = builder.sign(algorithm);
        LoginTokenRedis build = LoginTokenRedis.builder()
                .token(sign).userCode(subject)
                .alwaysOnline(alwaysOnline)
                .build();
        redisService.storageUserToken(build);
        return sign;
    }


    /**
     * 生成签名,并保存到redis中 (默认有过期时间)
     *
     * @param subject 用户唯一凭证(一般是登录名
     * @return 签名
     */
    public static String sign(String subject) {
        return sign(subject, false);
    }

    /**
     * 生成签名,并保存到redis中
     *
     * @param subject      用户唯一凭证(一般是登录名
     * @param alwaysOnline 是否永久在线
     * @return 签名
     */
    public static String sign(String subject, boolean alwaysOnline) {
        JwtBean jwtBean = ContextUtil.getBean(JwtBean.class);
        RedisService redisService = ContextUtil.getBean(RedisService.class);
        //过期时间
        Date date = new Date(System.currentTimeMillis() + jwtBean.getExpireTime());
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
        //设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        // jwt
        JWTCreator.Builder builder = JWT.create();

        //jwt header
        builder.withHeader(header);
        // 用户自定义字段
        builder.withClaim(JwtConstant.TOKEN_KEY, subject);
        // 签发时间
        builder.withIssuedAt(new Date());
        // 过期时间
        builder.withExpiresAt(date);
        // 发行人
        builder.withIssuer("jdevelops");
        // 主题
        builder.withSubject(subject);
        // 编号/版本
        builder.withJWTId(UUID.randomUUID().toString());
        // 生成token
        String sign = builder.sign(algorithm);
        LoginTokenRedis build = LoginTokenRedis.builder()
                .token(sign).userCode(subject)
                .alwaysOnline(alwaysOnline)
                .build();
        redisService.storageUserToken(build);
        return sign;
    }


    /**
     * token  验证token是否过期,如果未过期则刷新缓存中的token
     *
     * @param token token
     * @return true:没过期
     */
    public static boolean verity(String token) {
        try {
            JwtBean jwtBean = ContextUtil.getBean(JwtBean.class);
            RedisService redisService = ContextUtil.getBean(RedisService.class);
            Algorithm algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT verify = verifier.verify(token);
            redisService.refreshUserToken(verify.getSubject());
            return true;
        } catch (Exception e) {
            logger.error("token过期");
            return false;
        }
    }


}