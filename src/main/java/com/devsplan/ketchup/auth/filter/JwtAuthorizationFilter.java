//package com.devsplan.ketchup.auth.filter;
//
//
//
//import com.devsplan.ketchup.common.AuthConstants;
//import com.devsplan.ketchup.member.entity.Member;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.SignatureException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.json.simple.JSONObject;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetails;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
//public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
//    //프로바이더 인증이 완료되면 사용자정보를 토큰에 셋팅해주자
//
//    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
//        super(authenticationManager);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//        List<String> roleLessList = Arrays.asList("/signup"); //
//
//        if (roleLessList.contains((request.getRequestURI()))) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        String header = request.getHeader(AuthConstants.AUTH_HEADER);
//
//        try {
//            if (header != null && !header.equalsIgnoreCase("")) {
//                String token = TokenUtils.splitHeader(header);
//                //토큰에서 해더 부분 때고 보자
//
//                if (TokenUtils.isValidToken(token)) { //토큰이 유효한 토큰이야
//                    Claims claims = TokenUtils.getClaimsFromToken(token); //그럼 토큰에서 클레임 부분을 추출해서 저장해
//
//
//                    DetailsMember authentication = new DetailsMember(); // DetailsUser 형식의 인가를 만들고
//                    Member member = new Member(); //유저 형식의 새 유저를 만들어
//                    member.setMemberName(claims.get("userName").toString());  //클레임에서 userName부분을 때서 출력한 값으로 userName 셋팅
//                    member.getPosition().getAuthority().setRole(claims.get("Role").toString()); //같은방식으로 Role부분 때서 UserRole 셋팅
//                    authentication.setMember(member); // 인가에다가 해당 유저를 세팅
//
//
//                    //인증에 성공하고 나서 저장될 토큰의 타입이 AbstractAuthenticationToken 인거고
//                    // 담아주려하는 유저 디테일과 토큰과 권한정보를 넘겨주자
//                    AbstractAuthenticationToken authenticationToken
//                            = UsernamePasswordAuthenticationToken
//                            .authenticated(authentication, token, authentication.getAuthorities());
//                    authenticationToken.setDetails(new WebAuthenticationDetails(request));
//
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);//세팅 완료후 시큐리티 홀더에 저장
//                    //다 마쳤으면 다음 필터 알아서 돌아
//                    chain.doFilter(request, response);
//                } else {
//                    throw new RuntimeException("token이 유효하지 않습니다.");
//                }
//            } else {
//                throw new RuntimeException("token이 존재하지 않습니다.");
//            }
//        } catch (Exception e) {
//            response.setContentType("application/json");
//            PrintWriter printWriter = response.getWriter();
//            JSONObject jsonObject = jsonResponseWrapper(e);
//            printWriter.print(jsonObject);
//            printWriter.flush();
//            printWriter.close();
//        }
//    }
//
//    /**
//     * description. 토큰 관련 Exception 발생 시 예외 내용을 담은 객체 반환하는 메소드
//     *
//     * @param e : Exception
//     * @return JSONObject
//     */
//    private JSONObject jsonResponseWrapper(Exception e) {
//        String resultMsg = "";
//
//        if (e instanceof ExpiredJwtException) {
//            resultMsg = "Token Expired";
//        } else if (e instanceof SignatureException) {
//            resultMsg = "Token SignatureException";
//        } else if (e instanceof JwtException) {
//            resultMsg = "Token Parsing JwtException";
//        } else {
//            resultMsg = "other Token error";
//        }
//
//        HashMap<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("status", 401);
//        jsonMap.put("message", resultMsg);
//        jsonMap.put("reason", e.getMessage());
//
//        return new JSONObject(jsonMap);
//    }
//}
