package com.devsplan.ketchup.approval.controller;

import com.devsplan.ketchup.approval.dto.ApprovalDTO;
import com.devsplan.ketchup.approval.service.ApprovalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Slf4j
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService){
        this.approvalService = approvalService;
    }

    //기안 등록
    @PostMapping("/approvals")
    public String insertApproval(@RequestBody ApprovalDTO approvalDTO){

        return "";
    }


}
