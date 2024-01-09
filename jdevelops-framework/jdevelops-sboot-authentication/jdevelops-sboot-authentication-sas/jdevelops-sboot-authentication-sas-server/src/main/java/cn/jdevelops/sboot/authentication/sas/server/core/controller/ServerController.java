package cn.jdevelops.sboot.authentication.sas.server.core.controller;

import cn.jdevelops.sboot.authentication.sas.server.core.config.AuthorizationServerConfig;
import cn.jdevelops.sboot.authentication.sas.server.core.config.SasProperties;
import cn.jdevelops.sboot.authentication.sas.server.core.controller.dto.CustomRegisteredClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;
import java.util.UUID;

/**
 * 基础信息注册
 * @author tan
 */
@RestController
@Slf4j
public class ServerController {

    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final SasProperties sasProperties;

    public ServerController(RegisteredClientRepository registeredClientRepository,
                            PasswordEncoder passwordEncoder, SasProperties sasProperties) {
        this.registeredClientRepository = registeredClientRepository;
        this.passwordEncoder = passwordEncoder;
        this.sasProperties = sasProperties;
    }



    /**
     * 接口作用: 类似去微信平台平台申请 appId和appSecret一样
     */
    @PostMapping("/api/addClient")
    public String addClient(@RequestBody @Valid CustomRegisteredClient client) {
        // JWT（Json Web Token）的配置项：TTL、是否复用refreshToken等等
        TokenSettings tokenSettings = TokenSettings.builder()
                // 令牌存活时间：2小时
                .accessTokenTimeToLive(Duration.ofHours(sasProperties.getToken().getAccessTokenTimeToLive()))
                // 令牌可以刷新，重新获取
                .reuseRefreshTokens(sasProperties.getToken().getReuseRefreshTokens())
                // 刷新时间：30天（30天内当令牌过期时，可以用刷新令牌重新申请新令牌，不需要再认证）
                .refreshTokenTimeToLive(Duration.ofDays(sasProperties.getToken().getRefreshTokenTimeToLive()))
                .build();

        // 客户端相关配置
        ClientSettings clientSettings = ClientSettings.builder()
                // 是否需要用户授权确认[需要确认]
                .requireAuthorizationConsent(true)
                .build();

        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                // 客户端ID和密码
                .clientId(client.getClientId())
                .clientSecret(passwordEncoder.encode(client.getClientSecret()))
                // {noop}开头，表示“secret”以明文存储
//                .clientSecret("{noop}secret")
                .clientName(client.getClientName())
                // 授权方法
                .clientAuthenticationMethods(clientAuthenticationMethods -> {
                    clientAuthenticationMethods.addAll(client.getClientAuthenticationMethods());
                })
                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.addAll(client.getAuthorizationGrantTypes());
                })
//                // 授权模式（授权码模式）
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                // 刷新令牌（授权码模式）
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                // 回调地址：授权服务器向当前客户端响应时调用下面地址, 不在此列的地址将被拒绝, 只能使用IP或域名，不能使用 localhost
//                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
//                .redirectUri("http://127.0.0.1:8080/authorized")
                // 将上面的redirectUri地址注释掉，改成下面的地址，是因为我们暂时还没有客户端服务，以免重定向跳转错误导致接收不到授权码
                .redirectUris(uri -> {
                    uri.addAll(client.getRedirectUris());
                })
                // 设置客户端权限范围
                // OIDC 支持
                .scopes( scope -> {
//                    授权范围（当前客户端的授权范围）
                    scope.addAll(client.getScopes());
                })
                // JWT（Json Web Token）配置项
                .tokenSettings(tokenSettings)
                // 客户端配置项
                .clientSettings(clientSettings)
                .build();
        try {
            registeredClientRepository.save(registeredClient);
        }catch (IllegalArgumentException e){
            if(e.getMessage().contains("Registered client must be unique")){
               log.error("客户端已存在不要重复创建");
            }
            throw e;
        }
        return "添加客户端信息成功";
    }
}