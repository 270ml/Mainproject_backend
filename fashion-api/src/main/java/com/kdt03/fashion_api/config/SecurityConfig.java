package com.kdt03.fashion_api.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        @Resource(name = "${project.oauth2login.qualifier.name}")
        @Lazy
        private AuthenticationSuccessHandler oauth2SuccessHandler;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http,
                        com.kdt03.fashion_api.repository.MemberRepository memberRepo,
                        com.kdt03.fashion_api.util.JWTUtil jwtUtil,
                        HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository)
                        throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsSource()))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/members/logout").permitAll()
                                                .anyRequest().permitAll())
                                .formLogin(form -> form.disable())
                                .httpBasic(basic -> basic.disable());

                // OAuth2 로그인 설정
                http.oauth2Login(oauth2 -> oauth2
                                .authorizationEndpoint(authorization -> authorization
                                                .authorizationRequestRepository(cookieAuthorizationRequestRepository))
                                .successHandler(oauth2SuccessHandler));

                // 로그아웃 설정
                http.logout(logout -> logout
                                .logoutUrl("/api/members/logout")
                                .logoutSuccessHandler((request, response, authentication) -> {
                                        response.setStatus(org.springframework.http.HttpStatus.OK.value());
                                }));

                // JWT 필터
                http.addFilterBefore(
                                new com.kdt03.fashion_api.config.filter.JWTAuthorizationFilter(memberRepo, jwtUtil),
                                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        private CorsConfigurationSource corsSource() {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOriginPatterns(Arrays.asList("*"));
                config.addAllowedMethod(CorsConfiguration.ALL);
                config.addAllowedHeader(CorsConfiguration.ALL);
                config.setAllowCredentials(true);
                config.addExposedHeader(HttpHeaders.AUTHORIZATION);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
        }

}
