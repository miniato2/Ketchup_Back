package com.devsplan.ketchup.member.service;


import com.devsplan.ketchup.member.dto.DepDTO;
import com.devsplan.ketchup.member.dto.MemberDTO;
import com.devsplan.ketchup.member.dto.PositionDTO;
import com.devsplan.ketchup.member.entity.Dep;
import com.devsplan.ketchup.member.entity.Member;
import com.devsplan.ketchup.member.entity.Position;
import com.devsplan.ketchup.member.repository.DepRepository;
import com.devsplan.ketchup.member.repository.MemberRepository;
import com.devsplan.ketchup.member.repository.PositionRepository;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberService {


    private final ModelMapper modelMapper;
    private MemberRepository memberRepository;

    private DepRepository depRepository;

    private PositionRepository positionRepository;



    public MemberService(MemberRepository memberRepository, ModelMapper modelMapper, DepRepository depRepository,PositionRepository positionRepository) {

        this.modelMapper = modelMapper;
        this.memberRepository = memberRepository;
        this.depRepository = depRepository;
        this.positionRepository = positionRepository;

    }


    @Transactional
    public void insertMember (MemberDTO newMemberDTO){
        Member newMember = modelMapper.map(newMemberDTO, Member.class);

//       newMember.setMemberPW(passwordEncoder.encode(newMember.getMemberPW()));
       memberRepository.save(newMember);
    }

    @Transactional
    public void insertPosition(PositionDTO newPositionDTO){

        Position newPosition = modelMapper.map(newPositionDTO, Position.class);

        positionRepository.save(newPosition);



    }

    public Optional<Member> findUser(String memberName){
        System.out.println("여기는 오냐? 서비스 초입");
        Optional<Member> member = memberRepository.findByMemberName(memberName);

        System.out.println("여기는 끝난곳 서비스");

        /*
         * 별도의 검증 로직 작성
         * */

        return member;
    }

    public void insertDep(DepDTO newDepDTO) {
        Dep dep = modelMapper.map(newDepDTO, Dep.class);


        depRepository.save(dep);
    }


}
