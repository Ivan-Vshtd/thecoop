package com.example.thecoop.config;

import com.example.thecoop.utilities.RedirectInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.example.thecoop.controllers.ControllerUtils.parent;

/**
 * @author iveshtard
 * @since 7/31/2018
 */
@Slf4j
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.avatar.path}")
    private String uploadAvatarPath;

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public void addViewControllers(ViewControllerRegistry registry){

        log.debug("add registry page to view controller");
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        log.info("add locations to resource handler");
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///" +
                                parent(parent(parent(this
                                .getClass()
                                .getClassLoader()
                                .getResource("")
                                .getPath()))) +'/' + uploadPath + '/');

        registry.addResourceHandler("/avatar/**")
                .addResourceLocations("file:///" +
                                parent(parent(parent(this
                                .getClass()
                                .getClassLoader()
                                .getResource("")
                                .getPath()))) +'/' + uploadAvatarPath + '/');

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RedirectInterceptor());
    }
}
