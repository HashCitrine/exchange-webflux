package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Member;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MemberRepository extends R2dbcRepository<Member, String> {

    @Query("SELECT member_id, reg_date FROM member WHERE member_id = $1 and password = $2")
    Flux<Member> findByIdAndPassword(String memberId, String password);

    @Query("INSERT INTO member(member_id, password, role, use_yn, reg_date, upt_date) VALUES($1, $2, 'ADMIN', 'Y', current_timestamp, current_timestamp)")
    Mono<Member> register(String memberId, String password);

}
