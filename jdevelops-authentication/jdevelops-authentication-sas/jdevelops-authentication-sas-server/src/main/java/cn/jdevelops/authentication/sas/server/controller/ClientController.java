package cn.jdevelops.authentication.sas.server.controller;

import cn.jdevelops.api.result.response.PageResult;
import cn.jdevelops.api.result.response.ResultPageVO;
import cn.jdevelops.api.result.response.ResultVO;
import cn.jdevelops.authentication.sas.server.controller.dto.ClientEditScopes;
import cn.jdevelops.authentication.sas.server.controller.dto.ClientEditSecretExpiresAt;
import cn.jdevelops.authentication.sas.server.controller.dto.ClientPage;
import cn.jdevelops.authentication.sas.server.controller.dto.ClientRegistered;
import cn.jdevelops.authentication.sas.server.core.config.SasProperties;
import cn.jdevelops.authentication.sas.server.oauth.dao.Oauth2RegisteredClientDao;
import cn.jdevelops.authentication.sas.server.oauth.entity.Oauth2RegisteredClient;
import cn.jdevelops.authentication.sas.server.util.PageUtil;
import cn.jdevelops.util.authorization.error.exception.AuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static cn.jdevelops.authentication.sas.server.controller.specification.ClientSpecification.*;

/**
 * 基础信息注册
 * @author tan
 */
@RestController
@RequestMapping("/oauth/client")
@Slf4j
public class ClientController {

    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final Oauth2RegisteredClientDao oauth2RegisteredClientDao;
    private final SasProperties sasProperties;

    public ClientController(RegisteredClientRepository registeredClientRepository,
                            PasswordEncoder passwordEncoder,
                            Oauth2RegisteredClientDao oauth2RegisteredClientDao,
                            SasProperties sasProperties) {
        this.registeredClientRepository = registeredClientRepository;
        this.passwordEncoder = passwordEncoder;
        this.oauth2RegisteredClientDao = oauth2RegisteredClientDao;
        this.sasProperties = sasProperties;
    }


    /**
     * 接口作用: 类似去微信平台平台申请 appId和appSecret一样
     */
    @PostMapping("/add")
    public String addClient(@RequestBody @Valid ClientRegistered client) {
        // JWT（Json Web Token）的配置项：TTL、是否复用refreshToken等等
        TokenSettings tokenSettings = TokenSettings.builder()
                // 令牌存活时间：n小时
                .accessTokenTimeToLive(Duration.ofHours(sasProperties.getToken().getAccessTokenTimeToLive()))
                // 令牌可以刷新，重新获取
                .reuseRefreshTokens(sasProperties.getToken().getReuseRefreshTokens())
                // 刷新时间：n天（n天内当令牌过期时，可以用刷新令牌重新申请新令牌，不需要再认证）
                .refreshTokenTimeToLive(Duration.ofDays(sasProperties.getToken().getRefreshTokenTimeToLive()))
                .build();

        // 客户端相关配置
        ClientSettings clientSettings = ClientSettings.builder()
                // 是否需要用户授权确认[需要确认]
                .requireAuthorizationConsent(true)
                .build();

        RegisteredClient.Builder builder = RegisteredClient.withId(UUID.randomUUID().toString())
                // 客户端ID和密码
                .clientId(client.getClientId())
                .clientSecret(passwordEncoder.encode(client.getClientSecret()))
                // {noop}开头，表示“secret”以明文存储
//                .clientSecret("{noop}secret")
                .clientName(client.getClientName())
                // 授权方法
                .clientAuthenticationMethods(clientAuthenticationMethods ->
                        clientAuthenticationMethods.addAll(client.getClientAuthenticationMethods())
                )
                .authorizationGrantTypes(grantTypes -> grantTypes.addAll(client.getAuthorizationGrantTypes()))
                // 设置客户端权限范围（当前客户端的授权范围）
                .scopes(scope -> scope.addAll(client.getScopes()))
                // JWT（Json Web Token）配置项
                .tokenSettings(tokenSettings)
                // 客户端配置项
                .clientSettings(clientSettings);

        // 客户端回调地址
        if(client.getRedirectUris() != null){
            builder.redirectUris(uri -> uri.addAll(client.getRedirectUris()));
        }
        // 客户端过期时间，为空不设置，不设置等于不过期
        if(null != client.getClientSecretExpiresAt()){
            builder.clientSecretExpiresAt(
                    LocalDate.parse(client.getClientSecretExpiresAt())
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
                );
        }
        try {
            RegisteredClient registeredClient =builder.build();
            registeredClientRepository.save(registeredClient);
        }catch (Exception e){
            if(e.getMessage().contains("Registered client must be unique")){
               log.error("客户端已存在不要重复创建");
            }
            throw e;
        }
        return "添加客户端信息成功";
    }

    /**
     * 删除客户端
     * @param clientIds 客户端id
     * @return String
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<String> delete(@RequestBody List<String> clientIds){
        oauth2RegisteredClientDao.deleteByClientIdIn(clientIds);
        return ResultVO.successMessage("删除成功");
    }


    /**
     * 修改客户端 scopes
     * @param editScopes ClientEditScopes
     * @return String
     */
    @PostMapping("/edit/scopes")
    public ResultVO<String> editScopes(@RequestBody @Valid ClientEditScopes editScopes){
        Oauth2RegisteredClient client = verifyClient(editScopes.getClientId());
        client.setScopes(editScopes.getScopes());
        oauth2RegisteredClientDao.save(client);
        return ResultVO.successMessage("修改成功");
    }

    /**
     * 修改客户端 secret 过期时间
     * @param editSecretExpiresAt ClientEditSecretExpiresAt
     * @return String
     */
    @PostMapping("/edit/secretExpiresAt")
    public ResultVO<String> editScopes(@RequestBody @Valid ClientEditSecretExpiresAt editSecretExpiresAt){
        Oauth2RegisteredClient client = verifyClient(editSecretExpiresAt.getClientId());
        client.setClientSecretExpiresAt(LocalDate.parse(editSecretExpiresAt.getClientSecretExpiresAt())
                .atStartOfDay(ZoneId.systemDefault())
                .toLocalDateTime());
        oauth2RegisteredClientDao.save(client);
        return ResultVO.successMessage("修改成功");
    }




    /**
     * 分页查询客户端
     * @param page 客户端id
     * @return String
     */
    @PostMapping("/page")
    public ResultPageVO<PageResult<Oauth2RegisteredClient>> page(@RequestBody ClientPage page){
        Specification<Oauth2RegisteredClient> where = clientNameLike(page.getClientName())
                .and(scopesLike(page.getScopes()))
                .and(clientSecretExpiresAtSection(page.getClientSecretExpiresAtSection()));
        Page<Oauth2RegisteredClient> findAll = oauth2RegisteredClientDao
                .findAll(where, PageUtil.sortPageOf(page.getSortPage()));
        PageResult<Oauth2RegisteredClient> pages = PageUtil.toPage(findAll);
        return ResultPageVO.success(pages);
    }



    /**
     * 检查客户端是否存在
     * @param clientId 客户端ID
     * @return Oauth2RegisteredClient
     */
    private Oauth2RegisteredClient verifyClient(String clientId) {
        return oauth2RegisteredClientDao
                .findByClientId(clientId)
                .orElseThrow(() -> AuthorizationException.specialMessage("请检查客户端是否存在"));
    }
}
