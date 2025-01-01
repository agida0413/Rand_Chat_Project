package com.rand.jwt;


import java.nio.charset.StandardCharsets;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.rand.member.model.cons.MembersSex;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Slf4j
@Component
public final class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {

        //application.properties에 저장된 secretkey 암호 알고리즘 통해 생성사를 통해 secretkey 생성
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }


    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }


    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public JwtError validate(String token){
        JwtError jwtError = JwtError.CORRECT;
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        } catch (SignatureException e) {
             jwtError = JwtError.SIGNATURE;
        } catch (MalformedJwtException e) {
             jwtError = JwtError.MALFORM;
        } catch (ExpiredJwtException e) {
             jwtError = JwtError.EXPIRED;
        } catch (UnsupportedJwtException e) {
             jwtError = JwtError.UNSUPPORT;
        } catch (IllegalArgumentException e) {
             jwtError = JwtError.ILLEGAL;
        }

        return jwtError;
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category",String.class);
    }

    public String getNickname(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("nickname",String.class);
    }


    public int getUsrId(String token) {
        return Integer.parseInt(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("usrId",String.class));

    }

    public String getSex(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("sex",String.class);
    }

    public MembersSex getEnumSex(String token) {
        String strSex= Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("sex",String.class);

        if(strSex.equals(MembersSex.MAN)){
            return MembersSex.MAN;
        }
        else if(strSex.equals(MembersSex.FEMAIL)){
            return MembersSex.FEMAIL;
        }
       return MembersSex.MAN;
    }

    public String getFirstUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("firstUsrId",String.class);
    }
    public String getSecondUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("secondUsrId",String.class);
    }

    public LocalDate getBirth(String token) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr= Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("birth",String.class);
        LocalDate birth = LocalDate.parse(dateStr, formatter);
        return birth;
    }


    //토큰을 만듬
    public String createJwt(String category,String username,String usrId,String nickname,String sex,String birth, Long expiredMs) {

        return Jwts.builder()
                .claim("category", category) //refresh토큰인지 access 토큰인지
                .claim("username", username) //아이디
                .claim("usrId",usrId)//고유번호
                .claim("nickname", nickname)//닉네임
                .claim("sex",sex)
                .claim("birth",birth)
                .issuedAt(new Date(System.currentTimeMillis()))//만든날
                .expiration(new Date(System.currentTimeMillis() + expiredMs))//유효기간
                .signWith(secretKey)//시크릿키
                .compact();
    }


    //매칭수락 토큰
    public String createMatchingToken(String firstUsrId,String secondUsrId) {

        return Jwts.builder()
                .claim("firstUsrId", firstUsrId) //refresh토큰인지 access 토큰인지
                .claim("secondUsrId", secondUsrId) //아이디
                .issuedAt(new Date(System.currentTimeMillis()))//만든날
                .signWith(secretKey)//시크릿키
                .compact();
    }

}