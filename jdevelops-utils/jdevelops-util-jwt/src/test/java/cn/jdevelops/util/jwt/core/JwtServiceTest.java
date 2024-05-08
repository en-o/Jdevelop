package cn.jdevelops.util.jwt.core;

import cn.jdevelops.util.jwt.entity.LoginJwtExtendInfo;
import cn.jdevelops.util.jwt.entity.SignEntity;
import com.google.common.collect.ImmutableMap;
import junit.framework.TestCase;
import org.jose4j.lang.JoseException;

import java.util.Map;

public class JwtServiceTest extends TestCase {

    public void testGetLoginJwtExtendInfoExpires() throws JoseException {
        SignEntity<LoginJwtExtendInfo<String>> signEntity =
                new SignEntity<>("tan",  new LoginJwtExtendInfo<String>("tan","tan","tan"));
        String token = JwtService.generateToken(signEntity);
        LoginJwtExtendInfo<Map<String,String>> loginJwtExtendInfoExpires = JwtService.getLoginJwtExtendInfoExpires(token);
        assertEquals("LoginJwtExtendInfo{loginName='tan', userId='null', userNo='tan', userName='tan', phone='null', map=null}",
                loginJwtExtendInfoExpires.toString());


        SignEntity<LoginJwtExtendInfo<Map<String,String>>> signEntity2 =
                new SignEntity<>("tan",  new LoginJwtExtendInfo<Map<String,String>>("tan","tan","tan",
                        ImmutableMap.of("key","tan")));
        String token2 = JwtService.generateToken(signEntity2);
        LoginJwtExtendInfo<Map<String,String>> loginJwtExtendInfoExpires2 = JwtService.getLoginJwtExtendInfoExpires(token2);
        assertEquals("LoginJwtExtendInfo{loginName='tan', userId='null', userNo='tan', userName='tan', phone='null', map={\"key\":\"tan\"}}",
                loginJwtExtendInfoExpires2.toString());
    }

    public void testGenerateToken() throws JoseException {
        // 验证不同 tokenSecret  的 validateTokenByBoolean， 测试结果不同的 secret 不会出现混合验证成功的问题
        System.out.println(JwtService.generateToken("dasda"));
        System.out.println(JwtService.validateTokenByBoolean("eyJraWQiOiI1NTUzODk4ZC1jMzM0LTRlNTAtYjdjZS1jMjNkMTE2MDAxNzIiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJleHAiOjE3MTI5OTIxNDMsInN1YiI6ImRhc2RhIn0.c5B6RKqfy3oboqgHUVpN_RnuHkRRxjVWe8KSAvlFlUc"));
        System.out.println(JwtService.validateTokenByBoolean("eyJraWQiOiI4MmRiNDUyMi01OTE5LTRmM2MtODI0ZS1iNTIyOTQyMTEyMjUiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqZGV2ZWxvcHMiLCJhdWQiOiJqZGV2ZWxvcHMiLCJleHAiOjE3MTI5OTIyMTEsInN1YiI6ImRhc2RhIn0.9Drckx_WGWWkvV3QeXTKn4RC7W53o-dob2XwOubfY8g"));

    }
}
