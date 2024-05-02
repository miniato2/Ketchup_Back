package com.devsplan.ketchup.approval.controller;

import com.devsplan.ketchup.approval.dto.*;
import com.devsplan.ketchup.approval.service.ApprovalService;
import com.devsplan.ketchup.common.Pagenation;
import com.devsplan.ketchup.common.PagingButton;
import com.devsplan.ketchup.common.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class ApprovalController {

    private final ApprovalService approvalService;

    @Autowired
    public ApprovalController(ApprovalService approvalService){
        this.approvalService = approvalService;
    }

    //기안 등록
    @PostMapping("/approvals")
    public ResponseEntity<ResponseDTO> insertApproval(@ModelAttribute AppInputDTO appInputDTO,
                                                      List<MultipartFile> multipartFileList){

        log.info("==================================");
        log.info(appInputDTO.toString());

        return ResponseEntity.ok().body(
                new ResponseDTO(HttpStatus.OK, "기안상신 완료",
                        approvalService.insertApproval(appInputDTO, multipartFileList))
        );
    }

    @GetMapping("/approvals")
    public List<ApprovalDTO> selectApprovalList(){
        Page<ApprovalSelectDTO> appList = null;
        PagingButton paging = null;

        // 검색 결과
        /*switch (category){
            case 1:
                if(status == 전체){ status = 대기, 진행; }
                approvalService.selectMyApproval(member, status, search);
                break;
            case 2:
                if(status == 전체){ status = 완료, 반려, 회수; }
                approvalService.selectMyApproval(member, status, search);
                break;
            case 3:
                if(status == 전체){ status = 대기, 진행 }
                approvalService.selectReceiveApp(); //
                break;
            case 4:
                status = 완료
                approvalService.selectRefApp(); //상태 없음 => 무조건 승인된건만
                break;
        }*/
        return null;

    }

    //양식 조회
    @GetMapping("/forms/{formNo}")
    public ResponseEntity<ResponseDTO> selectForm(@PathVariable int formNo){

        return ResponseEntity.ok().body(
                new ResponseDTO(HttpStatus.OK, "양식조회 성공",
                        approvalService.selectForm(formNo))
        );
    }

    //기안 상태 수정
    @PutMapping("/approvals/{approvalNo}")
    public ResponseEntity<ResponseDTO> updateApprovals(@PathVariable int approvalNo,
                                                       @RequestBody AppUpdateDTO appUpdateDTO){
        String result = "";
        String memberNo = "1";

        if(appUpdateDTO.getAction().equals("회수")){
            result = approvalService.updateApproval(approvalNo);
        }else{
            result = approvalService.updateApproval2(appUpdateDTO, memberNo, approvalNo);
        }

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "성공", result));
    }
}
