package com.jarvis.app.auth.config;

import com.jarvis.app.auth.component.security.SocialMediaAuthenticationProvider;
import com.jarvis.app.auth.component.security.UserTokenEnhanceDetails;
import com.jarvis.app.auth.model.entity.UserAccount;
import com.jarvis.app.auth.service.security.UserAccountDetailsService;
import com.jarvis.app.auth.service.security.UserSocialMediaService;
import com.jarvis.frmk.core.util.RSAUtil;
import com.jarvis.frmk.hibernate.entity.ref.Status;
import com.jarvis.frmk.security.JarvisDaoAuthenticationProvider;
import com.jarvis.frmk.security.RSASecurityProvider;
import com.jarvis.frmk.security.RsaBCryptPasswordEncoder;
import com.jarvis.frmk.security.oauth2.token.JarvisTokenEnhancer;
import com.jarvis.frmk.security.oauth2.token.UuidAuthenticationKeyGenerator;
import com.jarvis.frmk.security.oauth2.token.store.JdbcInvalidateTokenStore;
import com.jarvis.frmk.security.service.CompositeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created: KimChheng
 * Date: 04-Oct-2019 Fri
 * Time: 10:32 PM
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
@EnableWebSecurity
@EntityScan(basePackageClasses = {UserAccount.class, Status.class})
@EnableTransactionManagement
public class AppConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RSASecurityProvider rsaProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailsUserDetailsService clientDetailService;

    @Autowired
    private UserAccountDetailsService userDetailService;

    @Autowired
    private UserSocialMediaService socialMediaService;

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        RsaBCryptPasswordEncoder passwordEncoder = new RsaBCryptPasswordEncoder();
        passwordEncoder.setRsaSecurityProvider(rsaProvider);
        passwordEncoder.setAllowPlainPassword(true);
        return passwordEncoder;
    }

    @Bean
    public TokenStore tokenStore() {
        JdbcInvalidateTokenStore tokenStore = new JdbcInvalidateTokenStore(dataSource);
        tokenStore.setAuthenticationKeyGenerator(new UuidAuthenticationKeyGenerator());
        return tokenStore;
    }

    @Bean
    public ClientDetailsService jdbcClientDetailsService() {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder());
        return clientDetailsService;
    }

    @Bean
    public ClientDetailsUserDetailsService clientDetailsUserDetailsService() {
        return new ClientDetailsUserDetailsService(jdbcClientDetailsService());
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        JarvisTokenEnhancer enhancer = new UserTokenEnhanceDetails();
        enhancer.includeAccessExpiry(true);
        enhancer.includeRefreshExpiry(true);
        enhancer.includeIssuedAt(true);
        return enhancer;
    }

    @Bean
    public RSASecurityProvider rsaSecurity() throws InvalidKeySpecException, IOException, NoSuchAlgorithmException {
        PrivateKey privateKey = RSAUtil.readPrivateKey(getClass().getResourceAsStream("/rsa-key/private-key.pem"));
        PublicKey publicKey = RSAUtil.readPublicKey(getClass().getResourceAsStream("/rsa-key/public-key.pem"));
        return new RSASecurityProvider(privateKey, publicKey);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        SocialMediaAuthenticationProvider socialMediaProvider = new SocialMediaAuthenticationProvider();
        socialMediaProvider.setSocialMediaService(socialMediaService);
        socialMediaProvider.setHideUserNotFoundExceptions(false);
        JarvisDaoAuthenticationProvider provider = new JarvisDaoAuthenticationProvider();
        provider.setUserDetailsService(new CompositeUserDetailsService(Arrays.asList(clientDetailService, userDetailService)));
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(false);
        return new ProviderManager(Collections.unmodifiableList(Arrays.asList(socialMediaProvider, provider)));
    }
}
