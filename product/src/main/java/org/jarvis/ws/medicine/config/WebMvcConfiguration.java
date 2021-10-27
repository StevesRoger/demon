package org.jarvis.ws.medicine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jarvis.core.component.WebMvcConfigurationAdapter;
import org.jarvis.core.jackson.Jackson2HttpMessageConverter;
import org.jarvis.core.jackson.module.JSONObjectModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import java.util.List;

/**
 * Created: KimChheng
 * Date: 21-Oct-2019 Mon
 * Time: 10:04 PM
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurationAdapter {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        // registry.addViewController("/login").setViewName("login");
        registry.addViewController("/403").setViewName("/error/403");
        registry.addViewController("/500").setViewName("/error/500");
        registry.addRedirectViewController("/swagger", "swagger-ui.html");
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
                .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2HttpMessageConverter messageConverter = new Jackson2HttpMessageConverter(mapper);
        mapper.registerModule(new JSONObjectModule());
        converters.add(0, messageConverter);
    }
}