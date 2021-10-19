package com.jarvis.app.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.jarvis.frmk.core.component.WebMvcConfigurerAware;
import com.jarvis.frmk.core.jackson.Jackson2HttpMessageConverter;
import com.jarvis.frmk.core.jackson.module.AliasFieldModule;
import com.jarvis.frmk.core.jackson.module.JSONObjectModule;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Created: KimChheng
 * Date: 21-Oct-2019 Mon
 * Time: 10:04 PM
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAware {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/403").setViewName("/error/403");
        registry.addViewController("/500").setViewName("/error/500");
        registry.addRedirectViewController("/swagger", "swagger-ui.html");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2HttpMessageConverter messageConverter = new Jackson2HttpMessageConverter(mapper);
        Hibernate5Module hibernate5Module = new Hibernate5Module(entityManagerFactory.unwrap(SessionFactory.class));
        hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        hibernate5Module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        hibernate5Module.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModules(hibernate5Module, new AliasFieldModule());
        mapper.registerModule(new JSONObjectModule());
        converters.add(0, messageConverter);
        super.extendMessageConverters(converters);
    }


    /*@Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
        return (container) -> {
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
            container.addErrorPages(error404Page);
        };
    }*/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*registry.addResourceHandler("/**")
                .addResourceLocations(
                        "classpath:/META-INF/resources/webjars/",
                        "classpath:/META-INF/resources",
                        "classpath:/resources/",
                        "classpath:/static/",
                        "classpath:/public/");*/
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}