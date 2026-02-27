package com.kdt03.fashion_api.config.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kdt03.fashion_api.domain.Member;
import com.kdt03.fashion_api.repository.MemberRepository;
import com.kdt03.fashion_api.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final MemberRepository memberRepo;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.equals("/api/imageupload/upload")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (jwtToken == null || !jwtToken.startsWith(JWTUtil.prefix)) {
            filterChain.doFilter(request, response);
            return;
        }
        String username = null;
        String provider = null;
        String email = null;

        try {
            username = jwtUtil.getClaim(jwtToken, JWTUtil.usernameClaim);
            provider = jwtUtil.getClaim(jwtToken, JWTUtil.providerClaim);
            email = jwtUtil.getClaim(jwtToken, JWTUtil.emailClaim);
        } catch (com.auth0.jwt.exceptions.JWTVerificationException e) {
            System.out.println("[JWTAuthorizationFilter] JWT Token invalid or expired: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"인증 토큰이 만료되었거나 유효하지 않습니다.\"}");
            return;
        }

        User user = null;
        Optional<Member> opt = memberRepo.findById(username);

        if (!opt.isPresent()) {
            // 데이터베이스는없지만provider, email이null이아니면
            // 데이터베이스에사용자를저장하지않는OAuth2 인증인경우
            if (provider == null || email == null) {
                System.out.println("[JWTAuthorizationFilter]not found user!");
                filterChain.doFilter(request, response);
                return;
            }
            System.out.println("[JWTAuthorizationFilter]username:" + username);
            user = new User(username, "****",
                    AuthorityUtils.createAuthorityList("ROLE_MEMBER"));
        } else {
            Member member = opt.get();
            System.out.println("[JWTAuthorizationFilter]" + member);
            user = new User(member.getId(), member.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_MEMBER"));
        }
        // 인증객체생성
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null,
                user.getAuthorities());
        // SecuirtyContext에등록
        SecurityContextHolder.getContext().setAuthentication(auth);
        // SecurityFilterChain의다음필터로이동
        filterChain.doFilter(request, response);
    }
}