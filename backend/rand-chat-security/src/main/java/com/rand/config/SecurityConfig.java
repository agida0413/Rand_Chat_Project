package com.rand.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.filter.CustomLogoutFilter;
import com.rand.filter.JWTFilter;
import com.rand.filter.LoginFilter;
import com.rand.jwt.JWTUtil;
import com.rand.member.repository.MemberRepository;
import com.rand.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    //JWTUtil 주입
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    //암호화
    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }


    //필터 및 시큐리티 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {



                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        // 여러 도메인 추가
                        configuration.setAllowedOrigins(Arrays.asList(
                                "http://localhost:3000", // 개발 환경 1
                                "http://randchat.o-r.kr", // 실제 서버
                                "https://randchat.o-r.kr" // 실제 서버 (HTTPS)
                        ));
                        configuration.setAllowedMethods(Collections.singletonList("*"));//모든메소드 허용
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("access"));

                        return configuration;
                    }
                })));

        http
                .csrf((auth) -> auth.disable()); //jwt 사용으로 인한 disable

        http
                .formLogin((auth) -> auth.disable()); //jwt사용으로 인한 기본로그인폼  x

        http
                .httpBasic((auth) -> auth.disable());


        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v1/member","/api/v1/reissue","/api/v1/member/email/**","/api/v1/member/unlock","match/test",
                                "/index.html","/css/**","/js/**","/images/**","/favicon.ico","/fonts/**","/img/**","/chat/ws").permitAll() //로그인 ,회원가입 , 토큰 재발급,이메일인증 api는 권한 필요없음
                        .requestMatchers("/api/v1/member/logout","/api/v1/member/location").authenticated()
                        .anyRequest().authenticated());//나머지는 인증이 필요함

      //  JWTFilter 등록 = > 로그인 필터 전에 수행
        http
                .addFilterBefore(new JWTFilter(jwtUtil,objectMapper,memberRepository), LoginFilter.class);


        // 로그인필터를  UsernamePasswordAuthenticationFilter 위치에
       http
               .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil,tokenService,objectMapper,memberRepository), UsernamePasswordAuthenticationFilter.class);

        //커스텀한 로그아웃 필터를 등록 =>기존 필터위치에
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, tokenService,objectMapper), LogoutFilter.class);

        // 세션방식 미사용
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
