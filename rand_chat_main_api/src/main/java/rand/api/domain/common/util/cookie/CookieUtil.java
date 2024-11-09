package rand.api.domain.common.util.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public  final class CookieUtil {

    /*
     * 쿠키 관련 유틸 클래스
     *
     */
    public static Object getCookie(String key, HttpServletRequest request) {

        Object object="";//리턴 오브젝터 초기화
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals(key)) {//쿠키를 읽어서 매개변수의 키값인것을 가져옴

                object= cookie.getValue(); // 그것에 대한 value를 읽음
            }
        }

        return object; // 가져온값 리턴
    }


    //쿠키생성 메서드
    public static void createCookie(String key, String value, int age, HttpHeaders httpHeaders) {//저장할 쿠키의 키값과 밸류를 매개변수로 받음

        Cookie cookie = new Cookie(key, value); // 매개변수로 부터 받은 정보를 바탕으로 쿠키 생성
        cookie.setMaxAge(age); // 유효기간
        //cookie.setSecure(true); https
        cookie.setPath("/");
        cookie.setHttpOnly(true); //xss 공격방지

        headerAdd(httpHeaders,cookie);
    }

    //쿠키제거 메서드
    public static void deleteRefreshCookie(String key, HttpHeaders httpHeaders) {//리프레시 토큰을 쿠키에서 지우기 위함
        Cookie cookie = new Cookie(key, null); // 리프레시 토큰의 밸류룰 null
        cookie.setMaxAge(0);//유효기간 0
        cookie.setPath("/");//path

        headerAdd(httpHeaders,cookie);
    }

    private static void headerAdd(HttpHeaders headers,Cookie cookie){
        headers.add("Set-Cookie", cookie.getName() + "=" + cookie.getValue() + "; Path=" + cookie.getPath()
                + "; HttpOnly; Max-Age=" + cookie.getMaxAge());
    }

}