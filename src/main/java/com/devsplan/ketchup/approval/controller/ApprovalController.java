package com.devsplan.ketchup.approval.controller;

import com.devsplan.ketchup.approval.dto.AppLineDTO;
import com.devsplan.ketchup.approval.dto.ApprovalDTO;
import com.devsplan.ketchup.approval.dto.RefLineDTO;
import com.devsplan.ketchup.approval.service.ApprovalService;
import com.devsplan.ketchup.common.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ResponseDTO> insertApproval(@ModelAttribute ApprovalDTO approvalDTO,
                                                      @ModelAttribute List<AppLineDTO> appLineDTOList,
                                                      @ModelAttribute List<RefLineDTO> refLineDTOList,
                                                      List<MultipartFile> multipartFileList){

        return ResponseEntity.ok().body(
                new ResponseDTO(HttpStatus.OK, "기안상신 완료",
                        approvalService.insertApproval(approvalDTO,appLineDTOList, refLineDTOList, multipartFileList))
        );
    }

    @GetMapping("/approvals")
    public List<ApprovalDTO> selectApprovalList(){
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
                if(status == 전체){ status = 대기, 진행 )
                approvalService.selectReceiveApp(); //
                break;
            case 4:
                status = 완료
                approvalService.selectRefApp(); //상태 없음 => 무조건 승인된건만
                break;

        }*/
        return null;

    }
}
