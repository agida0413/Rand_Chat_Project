package com.rand.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.custom.SecurityResponse;
import com.rand.entity.CustomUserDetails;
import com.rand.exception.custom.BadRequestException;
import com.rand.jwt.JWTUtil;
import com.rand.jwt.JwtError;
import com.rand.member.model.Members;
import com.rand.member.model.cons.MembersSex;
import com.rand.member.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import java.time.LocalDate;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;



    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // TODO Auto-generated method stub

        String requestURI = request.getRequestURI();//자원을 가져옴

        return requestURI.equals("/api/v1/reissue"); //재발급시에는 필터를 수행하지않음
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException {


        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {

            filterChain.doFilter(request, response);

            return;
        }
        logger.info("테스트?");
        logger.info(accessToken);
        //토큰 검증
        JwtError jwtError= jwtUtil.validate(accessToken);

        switch (jwtError){
            case SIGNATURE :
                SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-12");
                return;
            case ILLEGAL:
                SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-12");
                return;
            case MALFORM:
                SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-12");
                return;
            case UNSUPPORT:
                SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-12");
                return;
            case EXPIRED:
                SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-10");
                return;
        }
//        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
//        try {
//            jwtUtil.isExpired(accessToken);
//        } catch (ExpiredJwtException e) {
//
//            // 클라이언트 측에 410 에러전송 ,410 에러는 현재 서버내 유일하고 , 클라이언트 측에서는 응답 인터셉트로 받아 액세스토큰 재발급 진행
//
//
//            SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-10");
//            return;
//        }


        logger.info("구간1");

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            //액세스토큰이 아닐시
            SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-08");
            return;
        }
        logger.info("구간2");


        String username = jwtUtil.getUsername(accessToken);//이메일
        int usrId= jwtUtil.getUsrId(accessToken);//고유번호
        String nickname=jwtUtil.getNickname(accessToken);//닉네임
        MembersSex sex = jwtUtil.getEnumSex(accessToken);
        LocalDate birth = jwtUtil.getBirth(accessToken);

        Members members = new Members();
        members.setUsername(username);
        members.setUsrId(usrId);
        members.setNickName(nickname);
        members.setSex(sex);
        members.setBirth(birth);

        logger.info("구간3");



        CustomUserDetails customUserDetails = new CustomUserDetails(members);//ueserDetails에 dto객체 전달
        //일시적으로 세션에 담기위해 (SecurityContextHolder)
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(authToken);

        logger.info("구간4");

        filterChain.doFilter(request, response);
    }
}
