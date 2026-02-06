package com.kdt03.fashion_api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kdt03.fashion_api.domain.Member;
import com.kdt03.fashion_api.domain.dto.MemberLoginDTO;
import com.kdt03.fashion_api.domain.dto.MemberSignupDTO;
import com.kdt03.fashion_api.repository.MemberRepository;
import com.kdt03.fashion_api.domain.dto.MemberResponseDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepo;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void signup(MemberSignupDTO dto) {
        String raw = dto.getPassword();
        String encoded = passwordEncoder.encode(raw);

        Member member = Member.builder().id(dto.getId()).nickname(dto.getNickname()).password(encoded)
                .provider("local").build();

        memberRepo.save(member);
    }

    // 로그인
    public Member login(MemberLoginDTO dto) {
        Member member = memberRepo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 회원입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 다릅니다.");
        }

        return member;
    }

    // 회원 탈퇴
    public void withdraw(String memberId, String password) {
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new RuntimeException("가입되지 않은 회원입니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new RuntimeException("비밀번호가 다릅니다.");
        }

        memberRepo.delete(member);
    }

    // 전체 회원 조회 (테스트용)
    public java.util.List<MemberResponseDTO> getAllMembers() {
        return memberRepo.findAll().stream().map(member -> {
            String profile = member.getProfile();
            // Provider가 "local"이거나 null일 경우
            if ("local".equals(member.getProvider()) || member.getProvider() == null) {
                if (profile != null && !profile.startsWith("http")) { // 이미
                    profile = "http://10.125.121.182:8080" + profile;
                }
            }
            return MemberResponseDTO.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .provider(member.getProvider())
                    .profile(profile)
                    .build();
        }).collect(java.util.stream.Collectors.toList());
    }

}