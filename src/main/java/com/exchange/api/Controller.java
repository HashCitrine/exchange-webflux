package com.exchange.api;

import com.exchange.postgres.entity.Member;
import com.exchange.postgres.entity.Todo;
import com.exchange.postgres.repository.MemberRepository;
import com.exchange.postgres.repository.TodoRepository;
import com.exchange.postgres.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class Controller {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @GetMapping("index")
    public Mono<String> test() {
        Mono<String>  message = Mono.just("hello, world");
        return message;
    }
    @PostMapping("member")
    @ResponseStatus(HttpStatus.FOUND)
    public Flux<Member> getMember(@RequestBody Member member) {
        return memberService.login(member);
    }

    @PostMapping("/member/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Member> register(@RequestBody Member member) {
        return memberService.register(member);
    }

}
