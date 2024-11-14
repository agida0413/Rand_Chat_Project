package rand.chat.web.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rand.chat.domain.common.repository.InMemRepository;
import rand.chat.domain.common.util.cookie.CookieUtil;
import rand.chat.web.dto.common.ResponseDTO;
import rand.chat.web.exception.custom.InternerServerException;
import rand.chat.web.security.custom.SecurityResponse;
import rand.chat.web.security.jwt.JWTUtil;


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




}
