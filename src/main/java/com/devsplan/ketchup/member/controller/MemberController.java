package com.devsplan.ketchup.member.controller;




import com.devsplan.ketchup.member.dto.DepDTO;
import com.devsplan.ketchup.member.dto.MemberDTO;
import com.devsplan.ketchup.member.dto.PositionDTO;
import com.devsplan.ketchup.member.repository.MemberRepository;
import com.devsplan.ketchup.member.service.MemberService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MemberController {

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public MemberController (MemberService memberService, MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder){
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;

    }



    @PostMapping("/signup")
    public String signup(@RequestPart("memberInfo") MemberDTO newMemberDTO, @RequestPart("memberImage") MultipartFile memberImage){


        System.out.println("memberImage:   " + memberImage);

        int rPositionNo = newMemberDTO.getPosition().getPositionNo();

        System.out.println(rPositionNo);

        PositionDTO positionDTO = memberService.findPositionByPositionNo(rPositionNo);

        newMemberDTO.setPosition(positionDTO);

        int rDepNo = newMemberDTO.getDepartment().getDepNo();

        DepDTO depDTO = memberService.findDepByDepNo(rDepNo);

        newMemberDTO.setDepartment(depDTO);


        memberService.insertMember(newMemberDTO,memberImage);
        return "Member save!";


    }

    @PostMapping("/signupDep")
    public String signupDep(@RequestBody DepDTO depDTP){

        memberService.insertDep(depDTP);


        return "Dep save!";


    }

    @PostMapping("/signupPosition")
    public String signupPosition(@RequestBody PositionDTO positionDTO){

        memberService.insertPosition(positionDTO);


        return "Position save!";


    }

    @PutMapping ("/signupPosition/{positionNo}")
    public String updatePosition(@PathVariable int positionNo, @RequestBody PositionDTO positionDTO){

        positionDTO.setPositionNo(positionNo);
        memberService.updatePosition(positionDTO);


        return "Position update finish!";


    }





}

