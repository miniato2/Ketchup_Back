package com.devsplan.ketchup.approval;

import com.devsplan.ketchup.approval.dto.*;

import com.devsplan.ketchup.approval.repository.AppLineRepository;
import com.devsplan.ketchup.approval.service.ApprovalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ApprovalRestTests {
    @Autowired
    private ApprovalService service;
    @Autowired
    private AppLineRepository appRepository;

    @Description("기안 상신")
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

        //when
        String result = (String) service.insertApproval(approvalDTO, appLineDTOList, refLineDTOList, appFileList);

        //then
        Assertions.assertEquals(result, "성공");
    }

    @Description("내 기안 목록조회")
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

        //when
        List<ApprovalSelectDTO> approvalSelectDTOList = service.selectMyApproval(memberNo, status, searchValue);

        //then
        Assertions.assertNotNull(approvalSelectDTOList);
    }
    @Description("결재대기중인 기안 목록조회")
    @Test
    public void selectReceiveAppTest(){
        //given
        String memberNo = "2";
        String status1 = "대기";
        String status2 = "진행";
        String searchValue = "";

        List<String> status = new ArrayList<>();
        status.add(status1);
        status.add(status2);

        //then
        List<ApprovalSelectDTO> approvalSelectDTOList = service.selectReceiveApp(memberNo, status, searchValue);

        //when
        Assertions.assertNotNull(approvalSelectDTOList);
    }

    @Description("참조자 기안 목록조회")
    @Test
    public void selectRefAppTest(){
        //given
        String memberNo = "3";
        String status = "완료";
        String searchValue = "";

        //then
        List<ApprovalSelectDTO> approvalSelectDTOList = service.selectRefApp(memberNo, status, searchValue);

        //when
        Assertions.assertNotNull(approvalSelectDTOList);
    }

    @Test
    public void testRepo(){
        List<Integer> num = appRepository.findAppNoByMemberNo("2");
        Assertions.assertTrue(num.size() == 1);
    }
    @Test
    public void findAppline(){
        int appNo = 1;
        int count = appRepository.countSequence(appNo);

        Assertions.assertTrue(count == 2);
    }

    @Description("기안상세 조회")
    @Test
    public void selectApproval(){
        int appNo = 1;
        ApprovalSelectDTO approvalSelectDTO = service.selectApproval(appNo);
        Assertions.assertTrue(approvalSelectDTO.getApprovalNo() == 1);
    }

    @Description("기안 회수")
    @Test
    public void updateApproval() {
        int appNo = 1;
        String action = "회수";
        String memberNo = "1";

        AppUpdateDTO appUpdateDTO = new AppUpdateDTO();
        appUpdateDTO.setApprovalNo(appNo);
        appUpdateDTO.setAction(action);

        String result = (String) service.updateApproval(appUpdateDTO);

        Assertions.assertTrue(result == "성공");
    }

    @Description("기안 처리")
    @Test
    public void updateApprovaltest(){
        int appNo = 1;
        String action = "결재";
        String memberNo = "4";
        String refusal = "거절";

        AppUpdateDTO appUpdateDTO = new AppUpdateDTO(appNo, action, refusal);

        String result = (String) service.updateApproval2(appUpdateDTO, memberNo);

        //결재 처리시 필요한 정보 appNo, 행위(회수, 결재, 전결, 반려), refusal, memberNo
        Assertions.assertTrue(result == "성공");
    }





}
