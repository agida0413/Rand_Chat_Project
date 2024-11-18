package com.rand.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.rand.custom.SecurityResponse;
import com.rand.jwt.JWTUtil;
import com.rand.service.TokenService;
import com.rand.util.cookie.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;



import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;




    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {


        String requestUri = request.getRequestURI();//요청의 request url

        //api 요청이 /api/logout 이 아닐경우 다음필터로 넘김
        if (!requestUri.matches("^\\/api/v1/member/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod(); //post? get? put?

        if (!requestMethod.equals("POST")) {
            //만약 post 요청이 아닐경우 다음 필터로 넘김
            filterChain.doFilter(request, response);
            return;
        }

        //쿠키에서 refresh토큰을 가져옴


        String refresh = null;

        try {
            //쿠키를 읽어오는 메서드
            refresh=(String) CookieUtil.getCookie("refresh", request);

        } catch (Exception e) {
            // TODO: handle exception
            //쿠키 읽는 과정 에러 발생시
            SecurityResponse.writeErrorRes(response,objectMapper,"ERR-CMN-02");



            return;
        }
        //만약 refresh토큰이 없을 경우

        if (refresh == null) {


            SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-06"); //비 정상적인 접근입니다.
            return;
        }

        //유효기간 검증
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //refresh 쿠키제거메서드
            response.addCookie(CookieUtil.deleteRefreshCookie(refresh));

            SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-07");  //세션이 만료되었습니다.


            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {


            SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-08");  //비 정상적인 접근입니다.

            return;

        }
        String usrId = String.valueOf(jwtUtil.getUsrId(refresh)); // 레디스 키값
        //DB에 저장되어 있는지 확인
        Boolean isExist = tokenService.isExist(usrId,refresh);
        if (!isExist) {
        //비정상적인 토큰
            //refresh 쿠키제거메서드

            response.addCookie(CookieUtil.deleteRefreshCookie(refresh));
            SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-09");  //비 정상적인 접근입니다.
            return;
        }

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거

        tokenService.deleteRefresh(usrId, refresh);


        //Refresh 토큰 Cookie 값 0
        response.addCookie(CookieUtil.deleteRefreshCookie(refresh));//refresh 쿠키제거메서드
        response.setStatus(HttpServletResponse.SC_OK);

        //성공 응답값
        SecurityResponse.writeSuccessRes(response,objectMapper);
        return;
    }
}
