package com.devsplan.ketchup.member.controller;




import com.devsplan.ketchup.member.dto.DepDTO;
import com.devsplan.ketchup.member.dto.MemberDTO;
import com.devsplan.ketchup.member.dto.PositionDTO;
import com.devsplan.ketchup.member.repository.MemberRepository;
import com.devsplan.ketchup.member.service.MemberService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    public String signup(@RequestBody MemberDTO newMemberDTO){
        System.out.println("여기는 컨트롤러의 맴버등록 매소드야%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println(newMemberDTO);

        int rPositionNo = newMemberDTO.getPosition().getPositionNo();

        System.out.println(rPositionNo);

        PositionDTO positionDTO = memberService.findPositionByPositionNo(rPositionNo);

        newMemberDTO.setPosition(positionDTO);

        int rDepNo = newMemberDTO.getDepartment().getDepNo();

        DepDTO depDTO = memberService.findDepByDepNo(rDepNo);

        newMemberDTO.setDepartment(depDTO);


        memberService.insertMember(newMemberDTO);
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


        return "Position save!";


    }





}

