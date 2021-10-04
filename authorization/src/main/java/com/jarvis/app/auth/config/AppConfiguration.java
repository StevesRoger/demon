package com.jarvis.app.auth.config;

import com.jarvis.app.auth.component.security.UserTokenEnhanceDetails;
import com.jarvis.app.auth.model.entity.UserAccount;
import com.jarvis.frmk.core.util.RSAUtil;
import com.jarvis.frmk.hibernate.entity.ref.Status;
import com.jarvis.frmk.security.RSASecurityProvider;
import com.jarvis.frmk.security.RsaBCryptPasswordEncoder;
import com.jarvis.frmk.security.oauth2.token.JarvisTokenEnhancer;
import com.jarvis.frmk.security.oauth2.token.store.JdbcEvictTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

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
public class AppConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RSASecurityProvider rsaProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        RsaBCryptPasswordEncoder passwordEncoder = new RsaBCryptPasswordEncoder();
        passwordEncoder.setRsaSecurityProvider(rsaProvider);
        passwordEncoder.setAllowPlainPassword(true);
        return passwordEncoder;
    }

    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcEvictTokenStore(dataSource);
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
    public TokenEnhancer tokenEnhancer(UserTokenEnhanceDetails enhanceDetails) {
        JarvisTokenEnhancer enhancer = new JarvisTokenEnhancer();
        enhancer.setExpiresDate(true);
        enhancer.setRefreshExpiresDate(true);
        enhancer.setEnhanceDetails(enhanceDetails);
        return enhancer;
    }

    @Bean
    public RSASecurityProvider rsaSecurity() throws InvalidKeySpecException, IOException, NoSuchAlgorithmException {
        PrivateKey privateKey = RSAUtil.readPrivateKey(getClass().getResourceAsStream("/rsa-key/private-key.pem"));
        PublicKey publicKey = RSAUtil.readPublicKey(getClass().getResourceAsStream("/rsa-key/public-key.pem"));
        return new RSASecurityProvider(privateKey, publicKey);
    }
}
