package com.devsplan.ketchup.member.controller;



import com.devsplan.ketchup.member.entity.Member;
import com.devsplan.ketchup.member.repository.MemberRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private MemberRepository memberRepository;



    public MemberController (MemberRepository memberRepository){
        this.memberRepository = memberRepository;

    }

    @PostMapping("/signup")
    public String signup(@RequestBody Member member){
//        member.setMemberPW(passwordEncoder.encode(member.getMemberPW()));
        member.setState("Y");
        memberRepository.save(member);

        return "저장 완료";


    }





}
