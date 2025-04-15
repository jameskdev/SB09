package org.xm.sb09.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.xm.sb09.interceptors.FirstInterceptor;
import org.xm.sb09.interceptors.SecondInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FirstInterceptor())
                 .addPathPatterns("/test")
                 .order(1);
 
         registry.addInterceptor(new SecondInterceptor())
                 .addPathPatterns("/test")
                 .order(2);
    }
    
}
