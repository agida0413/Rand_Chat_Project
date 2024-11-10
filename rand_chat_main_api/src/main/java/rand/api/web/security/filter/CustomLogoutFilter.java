//package rand.api.web.security.filter;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.filter.GenericFilterBean;
//
//@RequiredArgsConstructor
//public class CustomLogoutFilter extends GenericFilterBean {
//
//    private final JWTUtil jwtUtil;
//    private final RefreshService refreshService;
//    private final ObjectMapper objectMapper;
//
//
//
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
//    }
//
//    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//
//
//        String requestUri = request.getRequestURI();//요청의 request url
//
//        //api 요청이 /api/logout 이 아닐경우 다음필터로 넘김
//        if (!requestUri.matches("^\\/api/logout$")) {
//
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String requestMethod = request.getMethod(); //post? get? put?
//
//        if (!requestMethod.equals("POST")) {
//            //만약 post 요청이 아닐경우 다음 필터로 넘김
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        //쿠키에서 refresh토큰을 가져옴
//
//
//        String refresh = null;
//
//        try {
//            //쿠키를 읽어오는 메서드
//            refresh=(String)CookieUtil.getCookie("refresh", request);
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            //쿠키 읽는 과정 에러 발생시
//            ResponseDTO<Void> responseApi = new ResponseDTO<Void>(
//                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                    "서버 내부오류입니다. 잠시 뒤 이용해주세요."
//            );
//            responseApi.set500Response(response, responseApi, objectMapper);
//            return;
//        }
//        //만약 refresh토큰이 없을 경우
//
//        if (refresh == null) {
//
//
//            ResponseDTO<Void> responseApi = new ResponseDTO<Void>(
//                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                    "세션이 만료되었습니다. 다시 로그인 해주세요."
//            );
//            responseApi.set500Response(response, responseApi, objectMapper);
//            return;
//        }
//
//        //유효기간 검증
//        try {
//            jwtUtil.isExpired(refresh);
//        } catch (ExpiredJwtException e) {
//
//            //refresh 쿠키제거메서드
//            response.addCookie(CookieUtil.deleteRefreshCookie());
//
//            //만료됬을 경우
//            ResponseDTO<Void> responseApi = new ResponseDTO<Void>(
//                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                    "세션이 만료되었습니다. 다시 로그인 해주세요."
//            );
//            responseApi.set500Response(response, responseApi, objectMapper);
//            return;
//        }
//
//        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
//        String category = jwtUtil.getCategory(refresh);
//        if (!category.equals("refresh")) {
//
//
//            ResponseDTO<Void> responseApi = new ResponseDTO<Void>(
//                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                    "비정상적인 접근입니다."
//            );
//            responseApi.set500Response(response, responseApi, objectMapper);
//            return;
//
//        }
//
//        //DB에 저장되어 있는지 확인
//        Boolean isExist = refreshService.isExist(refresh);
//        if (!isExist) {
//
//            //refresh 쿠키제거메서드
//            response.addCookie(CookieUtil.deleteRefreshCookie());
//            ResponseDTO<Void> responseApi = new ResponseDTO<Void>(
//                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                    "세션이 만료되었습니다. 다시 로그인 해주세요."
//            );
//            responseApi.set500Response(response, responseApi, objectMapper);
//            return;
//        }
//
//        //로그아웃 진행
//        //Refresh 토큰 DB에서 제거
//
//        refreshService.deleteRefresh(refresh);
//
//
//        //Refresh 토큰 Cookie 값 0
//        response.addCookie(CookieUtil.deleteRefreshCookie());//refresh 쿠키제거메서드
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        //성공 응답값
//        ResponseDTO<Void> succesResponseApi = new ResponseDTO<Void>();
//        succesResponseApi.setOkResponse(response, succesResponseApi, objectMapper);
//        return;
//    }
//}
