package com.devsplan.ketchup.member.repository;


import com.devsplan.ketchup.member.dto.MemberDTO;
import com.devsplan.ketchup.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {


    Optional<Member> findByMemberNo(String memberNo);

    @Transactional
    default void resignMember(String memberNo, MemberDTO memberDTO) {
      Member resignMember = findByMemberNo(memberNo).get();
      memberDTO.setMemberNo(memberNo);
      resignMember.setResignDateTime(memberDTO.getResignDateTime());
      resignMember.setStatus(memberDTO.getStatus());
       save(resignMember);
    }







}
