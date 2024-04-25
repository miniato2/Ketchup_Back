package com.devsplan.ketchup.member.repository;


import com.devsplan.ketchup.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {


    Optional<Member> findByMemberName(String memberName);

}
