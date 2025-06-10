package com.eos.admin.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	 @Value("${project.image.check-image}")
	    private String chequeImageDir;
	 @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry
	            .addResourceHandler("/images/**")
	            .addResourceLocations("file:///C:/Users/Bot_25/Desktop/Documents/CheckImages/");
	    }
}
