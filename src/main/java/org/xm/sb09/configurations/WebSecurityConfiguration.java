package org.xm.sb09.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfiguration {
    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
                .requestMatchers("/static/**");
    }

    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth ->              // 인증,인가 설정
                        auth.requestMatchers("/login", "/register", "/account").permitAll()
                                .requestMatchers(RegexRequestMatcher.regexMatcher(HttpMethod.GET, "/posts/.*")).permitAll()
                                .requestMatchers(RegexRequestMatcher.regexMatcher(HttpMethod.GET, "/test.*")).permitAll()
                                //.requestMatchers("edit-post").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .formLogin(auth -> auth.loginPage("/login")     // 폼 기반 로그인 설정
                        .defaultSuccessUrl("/posts/all", true))
                .logout(auth -> auth.logoutSuccessUrl("/login") // 로그아웃 설정
                        .invalidateHttpSession(true))
                .csrf(auth -> auth.disable());                  // csrf 비활성화
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
