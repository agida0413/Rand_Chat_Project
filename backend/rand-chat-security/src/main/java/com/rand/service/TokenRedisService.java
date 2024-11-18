package com.rand.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.common.ResponseDTO;
import com.rand.custom.SecurityResponse;
import com.rand.exception.custom.InternerServerException;
import com.rand.jwt.JWTUtil;
import com.rand.redis.InMemRepository;
import com.rand.util.cookie.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenRedisService implements TokenService{
    private final InMemRepository inMemRepository;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    @Override
    public void addRefresh(String key , String token) {
        TimeUnit timeUnit =TimeUnit.HOURS;

        long ttl = 24L;
        String rediskey = "refresh:"+key;
        inMemRepository.save(rediskey , token,ttl,timeUnit);
    }

    @Override
    public void deleteRefresh(String key , String token) {
        String rediskey = "refresh:"+key;

        String findToken =(String) inMemRepository.getValue(rediskey);

        if(findToken.equals(token)){
            inMemRepository.delete(rediskey);
        }

    }

    @Override
    public boolean isExist(String key,String token) {
       String redisKey = "refresh:"+key;
        String value = (String)inMemRepository.getValue(redisKey);

        if(value.equals(token)){
            return  true;
        }



        return false;
    }



    @Override
    //최종 refresh 토큰 발급 서비스
    public ResponseEntity<ResponseDTO<Void>> reissueToken(HttpServletRequest request, HttpServletResponse response) {


        String refresh = null;
        try {
            refresh=(String) CookieUtil.getCookie("refresh", request);

        } catch (Exception e) {
            // TODO: handle exception

            throw new InternerServerException("ERR-CMN-02"); //쿠키 추출에러

        }

        if (refresh == null) {//만약 refresh가 없다면


            try {
                SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-07"); // 세션만료
            } catch (IOException e) {
                throw new InternerServerException("ERR-CMN-01"); // 최상위 공통에러
            }
        }


        try {
            jwtUtil.isExpired(refresh);// 유효기간 검증
        } catch (ExpiredJwtException e) {

            response.addCookie(CookieUtil.deleteRefreshCookie(refresh));//refresh 쿠키제거메서드
            try {
                SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-07"); // 세션만료
            } catch (IOException ex) {
                throw new InternerServerException("ERR-CMN-01"); // 최상위 공통에러
            }

        }


        String category = jwtUtil.getCategory(refresh);   // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)

        if (!category.equals("refresh")) {//refresh 토큰이 아니면

            try {
                SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-08"); // 토큰 카테고리 다름
            } catch (IOException e) {
                throw new InternerServerException("ERR-CMN-01"); // 최상위 공통에러


            }

        }
        int usrId=jwtUtil.getUsrId(refresh); //고유번호를 읽어옴
        String strUsrId = String.valueOf(usrId);

        Boolean isExist = isExist(strUsrId,refresh); //DB에 저장되어 있는지 확인
        if (!isExist) {//없다면

            response.addCookie(CookieUtil.deleteRefreshCookie(refresh));//refresh 쿠키제거메서드
            try {
                SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-07"); // 세션만료
            } catch (IOException e) {
                throw new InternerServerException("ERR-CMN-01"); // 최상위 공통에러
            }
        }


        String userName = jwtUtil.getUsername(refresh); //토큰에서 이메일을 읽어옴
        String nickname=jwtUtil.getNickname(refresh);//닉네임 읽어옴
        String sex = jwtUtil.getSex(refresh);
        String strBirth = jwtUtil.getBirth(refresh).toString();

        //새로운 jwt 토큰 발급
        String newAccess = jwtUtil.createJwt("access", userName, strUsrId,nickname, sex, strBirth,300000L);//엑세스 토큰
        String newRefresh = jwtUtil.createJwt("refresh", userName, strUsrId,nickname ,sex,strBirth,86400000L); //리프레시 토큰



        deleteRefresh(strUsrId,refresh); //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장


        addRefresh(strUsrId,newRefresh); //새토큰 저장


        //응답 설정
        response.setHeader("access", newAccess);//엑세스 토큰은 헤더에

        Cookie cookie= CookieUtil.createCookie("refresh",newRefresh,300);

        //성공시 응답
        response.addCookie(cookie);


        return ResponseEntity.ok(new ResponseDTO<Void>(null));
    }
}
