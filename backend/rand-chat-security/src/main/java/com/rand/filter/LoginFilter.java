package com.rand.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.custom.SecurityContextGet;
import com.rand.custom.SecurityResponse;
import com.rand.jwt.JWTUtil;
import com.rand.member.model.cons.MembersState;
import com.rand.member.repository.MemberRepository;
import com.rand.service.TokenService;
import com.rand.util.cookie.CookieUtil;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;




import java.time.LocalDate;
import java.util.Collections;
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    //jwt
    private final JWTUtil jwtUtil;
    //액세스 토큰 재발급 서비스
    private final TokenService tokenService;

    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,TokenService tokenService,ObjectMapper objectMapper,MemberRepository memberRepository) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
        this.memberRepository = memberRepository;

        //로그인 api url
        setFilterProcessesUrl("/api/v1/member/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {



        String username=obtainUsername(request); //프론트엔드에서 username으로 formdata로 준값 읽기
        String password=obtainPassword(request); //프론트엔드에서 password로 formdata로 준값 읽기


        //아이디 , 패스원드  VALIDATION
        /********************/

        if(username==null || password == null || username.trim().equals("") || password.trim().equals("")){
            try {
                SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-02");


            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }

            return null;
        }





        //로그인을 위해  UsernamePasswordAuthenticationToken 에 정보를 담고 authenticate= > userdetailservice = > 인가 권한정보 없음
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());

        return authenticationManager.authenticate(authToken);
    }


    //로그인 인증 성공시
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, java.io.IOException {






        int usrId= SecurityContextGet.getUsrId(authentication);
        String nickname=SecurityContextGet.getNickname(authentication);
        String userName =SecurityContextGet.getUsername(authentication);
        String sex = SecurityContextGet.getSex(authentication);
        LocalDate birth = SecurityContextGet.getBirth(authentication);

        log.info("usrId={}",usrId);
        log.info("nickname={}",nickname);
        log.info("username={}",userName);
        log.info("sex={}",sex);
        log.info("birth={}",birth);

        //문자열로 변환
        String strUsrId=String.valueOf(usrId);
        String strBirth = birth.toString();


        //정지회원 처리

        String state = SecurityContextGet.getState(authentication);

        log.info("sadadad={}",state);
        //탈퇴계정 (유예기간)
        if(state.equals(MembersState.SUSPENDED.toString())){
            SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-03");
            return;
        }
        //잠긴 계정
        if(state.equals(MembersState.LOCKED.toString())){
            SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-04");
            return;
        }

        if(state.equals(MembersState.INACTIVE.toString())){
            SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-05");
            return;
        }

        //String category,String username,String usrId,
        // String nickname,String sex,String birth, Long expiredMs
        //토큰 생성( 각토큰이름 + email+role+strIdNum + 유효기간 + 시크릿키(sha))
        String access = jwtUtil.createJwt("access", userName, strUsrId,nickname, sex, strBirth,300000L);//엑세스 토큰
        String refresh = jwtUtil.createJwt("refresh", userName, strUsrId,nickname ,sex,strBirth,86400000L); //리프레시 토큰


        //refresh토큰 레디스에 저장 = > 서버에서 제어권을 가지려고 ( 나중에 탈취당했을때에 대비하여)
        tokenService.addRefresh(strUsrId,refresh);

        //응답 설정
        response.setHeader("access", access);//엑세스 토큰은 헤더에

       Cookie cookie= CookieUtil.createCookie("refresh",refresh,300);

        //성공시 응답
        response.addCookie(cookie);

        SecurityResponse.writeSuccessRes(response,objectMapper);

        return;


    }


    //실패시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws java.io.IOException {
        String username=obtainUsername(request); //프론트엔드에서 username으로 formdata로 준값 읽기



      SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-01");

       //실패시 예외 발생 및 틀린횟수 ++

        memberRepository.pwdWrongUpdate(username);
    }




}
