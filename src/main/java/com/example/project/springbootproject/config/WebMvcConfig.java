package com.example.project.springbootproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
            .allowedMethods(HttpMethod.GET.toString()
                , HttpMethod.POST.toString()
                , HttpMethod.PUT.toString()
                , HttpMethod.PATCH.toString()
                , HttpMethod.DELETE.toString())
            .allowedHeaders("*");
    }
}
