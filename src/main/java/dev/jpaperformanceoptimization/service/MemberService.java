package dev.jpaperformanceoptimization.service;

import dev.jpaperformanceoptimization.domain.Member;
import dev.jpaperformanceoptimization.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = findOne(id);
        member.setName(name);
        //dirty check: update query
    }

    public Member findOne(Long id) {
        return memberRepository.findById(id)
                .orElseThrow();
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
}
