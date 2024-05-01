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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final ModelMapper modelMapper;

    private final MemberRepository memberRepository;

    private final DepRepository depRepository;

    private final PositionRepository positionRepository;

    private final PasswordEncoder passwordEncoder;



    public MemberService(MemberRepository memberRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder,DepRepository depRepository,PositionRepository positionRepository) {
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.memberRepository = memberRepository;
        this.depRepository = depRepository;
        this.positionRepository = positionRepository;

    }


    @Transactional
    public void insertMember (MemberDTO newMemberDTO){
        Member newMember = modelMapper.map(newMemberDTO, Member.class);
        newMember.setMemberPW(passwordEncoder.encode(newMember.getMemberPW()));
        memberRepository.save(newMember);
    }



    @Transactional
    public void insertPosition(PositionDTO newPositionDTO){

        System.out.println("DTO : "+newPositionDTO);

        Position newPosition = modelMapper.map(newPositionDTO, Position.class);

        System.out.println("ENTITY : "+ newPosition);

        positionRepository.save(newPosition);
    }

    @Transactional
    public void updatePosition(PositionDTO updatePositionDTO){

        System.out.println("수정할 직급 : "+updatePositionDTO);

        Position updatePosition = modelMapper.map(updatePositionDTO, Position.class);

        System.out.println("ENTITY : "+ updatePosition);

        positionRepository.save(updatePosition);
    }


    public Optional<Member> findMember(String memberNo){

        Optional<Member> member = memberRepository.findByMemberNo(memberNo);

        /*
         * 별도의 검증 로직 작성
         * */

        return member;
    }

    @Transactional
    public void insertDep(DepDTO newDepDTO) {
        Dep dep = modelMapper.map(newDepDTO, Dep.class);


        depRepository.save(dep);
    }


    public PositionDTO findPositionByPositionNo(int rPositionNo) {
        Position  rPosition =  positionRepository.findPositionByPositionNo(rPositionNo);
        PositionDTO rPositionDTO = modelMapper.map(rPosition, PositionDTO.class);
        return rPositionDTO;
    }

    public DepDTO findDepByDepNo(int rDepNo) {
        Dep rDep = depRepository.findDepByDepNo(rDepNo);
        DepDTO rDepDTO = modelMapper.map(rDep,DepDTO.class);

        return rDepDTO;
    }

    public void resignMember(String memberNo, MemberDTO memberDTO) {
        memberDTO.setResignDateTime(LocalDateTime.now());
        memberRepository.resignMember(memberNo,memberDTO);

    }





}
