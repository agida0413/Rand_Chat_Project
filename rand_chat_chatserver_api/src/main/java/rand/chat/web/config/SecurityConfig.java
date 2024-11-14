package rand.chat.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import rand.chat.web.security.filter.JWTFilter;
import rand.chat.web.security.jwt.JWTUtil;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    //JWTUtil 주입
    private final JWTUtil jwtUtil;
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
                .csrf((auth) -> auth.disable()); //jwt 사용으로 인한 disable

        http
                .formLogin((auth) -> auth.disable()); //jwt사용으로 인한 기본로그인폼  x

        http
                .httpBasic((auth) -> auth.disable());

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/index.html","/css/**","/js/**","/images/**","/favicon.ico","/fonts/**","/img/**").permitAll() //로그인 ,회원가입 , 토큰 재발급,이메일인증 api는 권한 필요없음
                        .anyRequest().authenticated());//나머지는 인증이 필요함

      //  JWTFilter 등록 = > 로그인 필터 전에 수행
        http
                .addFilterBefore(new JWTFilter(jwtUtil,objectMapper), UsernamePasswordAuthenticationFilter.class);




        // 세션방식 미사용
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
