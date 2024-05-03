package com.devsplan.ketchup.member.controller;




import com.devsplan.ketchup.common.Criteria;
import com.devsplan.ketchup.common.PageDTO;
import com.devsplan.ketchup.common.PagingResponseDTO;
import com.devsplan.ketchup.common.ResponseDTO;
import com.devsplan.ketchup.member.dto.DepDTO;
import com.devsplan.ketchup.member.dto.MemberDTO;
import com.devsplan.ketchup.member.dto.PositionDTO;
import com.devsplan.ketchup.member.repository.MemberRepository;
import com.devsplan.ketchup.member.service.MemberService;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MemberController {

    private final MemberService memberService;



    public MemberController (MemberService memberService){
        this.memberService = memberService;


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


    @GetMapping("/members/{memberNo}")
    public ResponseEntity<ResponseDTO> findMember(@PathVariable String memberNo){

      MemberDTO findMember  =  memberService.findMember(memberNo).get();

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"조회성공",findMember));

    }


    @GetMapping("/members")
    public ResponseEntity<ResponseDTO> findAllMembers(
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "offset", defaultValue = "1"
            ) String offset){

        Criteria cri = new Criteria(Integer.valueOf(offset),10);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        /* 1. offset의 번호에 맞는 페이지에 뿌려줄 Member들 */

        Page<MemberDTO> memberList = memberService.findAllMembersWithPaging(cri,search);
        pagingResponseDTO.setData(memberList);

        /* 2. PageDTO : 화면에서 페이징 처리에 필요한 정보들 */
        pagingResponseDTO.setPageInfo(new PageDTO(cri, (int) memberList.getTotalElements()));


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"조회성공",pagingResponseDTO));

    }



    @GetMapping ("/Email/{memberNo}")
    public String sendVerifyEmail(@PathVariable String memberNo){
        System.out.println(memberNo);

        String verifyCode = memberService.sendVerifyEmail(memberNo);

        return verifyCode;


    }








}

