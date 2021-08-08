package com.exchange.postgres.service;

import com.exchange.postgres.entity.Member;
import com.exchange.postgres.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Flux<Member> findAll() {
        return memberRepository.findAll();
    }

    public Flux<Member> login(Member member) {
        return memberRepository.findByIdAndPassword(member.getMemberId(), member.getPassword());
    }

    public Mono<Member> register(Member member) {
        return memberRepository.register(member.getMemberId(), member.getPassword());
    }

}
