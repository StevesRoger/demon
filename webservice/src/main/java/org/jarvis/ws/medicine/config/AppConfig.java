package org.jarvis.ws.medicine.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.jarvis.core.annotation.EnableJarvisDefaultErrorHandler;
import org.jarvis.core.annotation.EnableLogSuffixInjector;
import org.jarvis.orm.mybatis.IBatisConfiguration;
import org.jarvis.orm.mybatis.annotation.IBatisRepository;
import org.jarvis.security.ISecurity;
import org.jarvis.security.oauth2.JarvisOAuth2AuthenticationEntryPoint;
import org.jarvis.ws.medicine.component.MedicineInterceptor;
import org.jarvis.ws.medicine.handler.AnyEnumHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

/**
 * Created by KimChheng on 7/6/2019.
 */
@Configuration
@EnableLogSuffixInjector
@EnableJarvisDefaultErrorHandler
@EnableScheduling
@EnableAsync
public class AppConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        IBatisConfiguration configuration = new IBatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setEnvironment("development");
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage("org.jarvis.ws.medicine.model");
        sessionFactory.setConfiguration(configuration);
        sessionFactory.setDefaultEnumTypeHandler(AnyEnumHandler.class);
        sessionFactory.setPlugins(new MedicineInterceptor());
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/*.xml"));
        return sessionFactory.getObject();
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
        scannerConfigurer.setBasePackage("org.jarvis.ws.medicine.repository");
        scannerConfigurer.setAnnotationClass(IBatisRepository.class);
        scannerConfigurer.setLazyInitialization("true");
        return scannerConfigurer;
    }

    @Bean
    public JarvisOAuth2AuthenticationEntryPoint auth2AuthenticationEntryPoint() {
        return new JarvisOAuth2AuthenticationEntryPoint(ISecurity.REALM_NAME);
    }

}
