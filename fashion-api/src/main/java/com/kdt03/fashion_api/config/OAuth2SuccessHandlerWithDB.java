package com.kdt03.fashion_api.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kdt03.fashion_api.domain.Member;
import com.kdt03.fashion_api.repository.MemberRepository;
import com.kdt03.fashion_api.util.JWTUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component("OAuth2SuccessHandlerWithDB")
@RequiredArgsConstructor
public class OAuth2SuccessHandlerWithDB extends OAuth2SuccessHandler {
    private final MemberRepository memRepo;
    private final PasswordEncoder encoder;
    private final JWTUtil jwtUtil;

    //  @Autowired
    // public void setMemRepo(MemberRepository memRepo) {
    //     this.memRepo = memRepo;
    // }

    // @Autowired
    // public void setEncoder(PasswordEncoder encoder) {
    //     this.encoder = encoder;
    // }
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, String> map = getUserInfo(authentication);
        String username = map.get("provider") + "_"+ map.get("email");
        memRepo.save(Member.builder().id(username)
        .password(encoder.encode("1a2s3d4f"))
        .nickname(map.get("name"))
        .provider(map.get("provider"))
        .build());
        String token = jwtUtil.getJWT(username); // JWT 생성
        sendJWTtoClient(response, token);
        // response.sendRedirect("/test-page");
        response.sendRedirect("/test-page?token=" + token);
    }
}
