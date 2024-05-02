package com.devsplan.ketchup.approval;

import com.devsplan.ketchup.approval.dto.*;

import com.devsplan.ketchup.approval.repository.AppLineRepository;
import com.devsplan.ketchup.approval.service.ApprovalService;
import com.devsplan.ketchup.member.dto.MemberDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class ApprovalRestTests {
    @Autowired
    private ApprovalService service;
    private static final Logger logger = LoggerFactory.getLogger(ApprovalRestTests.class);

    @DisplayName("기안 상신")
    @Test
    public void insertApprovalTest(){
        //given
        ApprovalDTO approvalDTO = new ApprovalDTO("1", 1, "제목", "내용");
        AppLineDTO appLineDTO = new AppLineDTO("2", 1, "일반");
        AppLineDTO appLineDTO2 = new AppLineDTO("4", 2, "전결");
        RefLineDTO refLineDTO = new RefLineDTO("3");

        List<AppLineDTO> appLineDTOList = new ArrayList<>();
        appLineDTOList.add(appLineDTO);
        appLineDTOList.add(appLineDTO2);

        List<RefLineDTO> refLineDTOList = new ArrayList<>();
        refLineDTOList.add(refLineDTO);

        List<MultipartFile> appFileList = new ArrayList<>();
        MockMultipartFile multipartFile = new MockMultipartFile("image", "image.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());
        appFileList.add(multipartFile);

        AppInputDTO appInputDTO = new AppInputDTO(approvalDTO, appLineDTOList, refLineDTOList);
//        logger.info(multipartFile.toString());
//
//        Assertions.assertTrue(true);

        //when
        String result = (String) service.insertApproval(appInputDTO, appFileList);

        //then
        Assertions.assertEquals(result, "성공");

    }


    @DisplayName("내 기안 목록조회")
    @Test
    public void selectMyApprovalTest(){
        //given
        String memberNo = "1";
        String status1 = "대기";
        String status2 = "진행";
        String searchValue = "제목";

        List<String> status = new ArrayList<>();
        status.add(status1);
        status.add(status2);


//        Pageable page = null;

        //when
        List<ApprovalSelectDTO> approvalSelectDTOList = service.selectMyApproval(memberNo, status, searchValue);

        //then
        Assertions.assertTrue(approvalSelectDTOList.size() == 4);
    }

    @DisplayName("결재대기중인 기안 목록조회")
    @Test
    public void selectReceiveAppTest(){
        //given
        String memberNo = "2";
        String status1 = "대기";
        String status2 = "진행";
        String searchValue = "제";

        List<String> status = new ArrayList<>();
        status.add(status1);
        status.add(status2);

        //then
        List<ApprovalSelectDTO> approvalSelectDTOList = service.selectReceiveApp(memberNo, status, searchValue);
        logger.info(approvalSelectDTOList.toString());

        //when
        Assertions.assertNotNull(approvalSelectDTOList);
    }

    @DisplayName("참조자 기안 목록조회")
    @Test
    public void selectRefAppTest(){
        //given
        String memberNo = "3";
        String status = "완료";
        String searchValue = "";

        //then
        List<ApprovalSelectDTO> approvalSelectDTOList = service.selectRefApp(memberNo, status, searchValue);
        logger.info(approvalSelectDTOList.toString());

        //when
        Assertions.assertNotNull(approvalSelectDTOList);
    }

    @DisplayName("기안상세 조회")
    @Test
    public void selectApproval(){
        int appNo = 1;
        ApprovalSelectDTO approvalSelectDTO = service.selectApproval(appNo);
        Assertions.assertTrue(approvalSelectDTO.getApprovalNo() == 1);
    }

    @DisplayName("기안 회수")
    @Test
    public void updateApproval() {
        int appNo = 1;
        String action = "회수";

        AppUpdateDTO appUpdateDTO = new AppUpdateDTO();
        appUpdateDTO.setAction(action);

        String result = service.updateApproval(appNo);

        Assertions.assertTrue(result == "성공");
    }

    @DisplayName("기안 처리")
    @Test
    public void updateApprovaltest(){
        int appNo = 1;
        String action = "결재";
        String memberNo = "4";
        String refusal = "거절";

        AppUpdateDTO appUpdateDTO = new AppUpdateDTO(action, refusal);

        String result = service.updateApproval2(appUpdateDTO, memberNo, appNo);

        //결재 처리시 필요한 정보 appNo, 행위(회수, 결재, 전결, 반려), refusal, memberNo
        Assertions.assertTrue(result == "성공");
    }

    @DisplayName("양식 조회")
    @Test
    public void selectFormTest(){
        int formNo = 1;

        Assertions.assertNotNull(service.selectForm(formNo));
    }





}
