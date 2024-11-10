package rand.api.web.config;

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
import rand.api.domain.member.repository.MemberRepository;
import rand.api.web.security.filter.LoginFilter;
import rand.api.web.security.jwt.JWTUtil;
import rand.api.web.security.service.TokenService;

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
                .csrf((auth) -> auth.disable()); //jwt 사용으로 인한 disable

        http
                .formLogin((auth) -> auth.disable()); //jwt사용으로 인한 기본로그인폼  x

        http
                .httpBasic((auth) -> auth.disable());

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v1/member/**",
                                "/index.html","/css/**","/js/**","/images/**","/favicon.ico","/fonts/**","/img/**").permitAll() //로그인 ,회원가입 , 토큰 재발급,이메일인증 api는 권한 필요없음
                        .anyRequest().authenticated());//나머지는 인증이 필요함

        //JWTFilter 등록 = > 로그인 필터 전에 수행
//        http
//                .addFilterBefore(new JWTFilter(jwtUtil,objectMapper,memberAccountRepository), LoginFilter.class);


        // 로그인필터를  UsernamePasswordAuthenticationFilter 위치에
       http
               .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil,tokenService,objectMapper,memberRepository), UsernamePasswordAuthenticationFilter.class);

//        //커스텀한 로그아웃 필터를 등록 =>기존 필터위치에
//        http
//                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshService,objectMapper), LogoutFilter.class);

        // 세션방식 미사용
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
