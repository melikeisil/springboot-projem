package com.ownify.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        
        registry.addViewController("/dashboard").setViewName("forward:/dashboard.html");
        registry.addViewController("/signin").setViewName("forward:/signin.html");
        registry.addViewController("/signup").setViewName("forward:/signup.html");
        registry.addViewController("/forgotPass").setViewName("forward:/forgotPass.html");
        registry.addViewController("/dashboardSetting").setViewName("forward:/dashboardSetting.html");
        registry.addViewController("/adPosting").setViewName("forward:/adPosting.html");
        registry.addViewController("/wishlist").setViewName("forward:/wishlist.html");
    }
}
