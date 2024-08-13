package com.ohgiraffers.chat.login.config;

import com.ohgiraffers.chat.login.handler.CustomAccessHandler;
import com.ohgiraffers.chat.login.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final CustomAccessHandler customAccessHandler;

    @Autowired
    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomAccessHandler customAccessHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customAccessHandler = customAccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .formLogin((login) -> login.disable())
                .httpBasic((basic) -> basic.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/oauth2/**", "/login/**","/signup/**","/static/**", "/image/**", "/img/**", "/css/**", "/javascript/**","/font/**").permitAll()
                        .requestMatchers("/logininfo/userinfo").authenticated()
                        .anyRequest().authenticated())
                .oauth2Login((oauth2) -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/main",true)// 로그인 성공하면 여기로 리다이렉트
                        .userInfoEndpoint((userInfoEndpointConfig) ->
                                userInfoEndpointConfig.userService(customOAuth2UserService)))
                .logout(logout -> logout
                        .logoutSuccessHandler(logoutSuccessHandler())
                        .permitAll())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler(customAccessHandler));

        return http.build();
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
        logoutSuccessHandler.setDefaultTargetUrl("/"); // 로그아웃후에 매핑할 주소
        return logoutSuccessHandler;
    }

}