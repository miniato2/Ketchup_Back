package com.devsplan.ketchup.approval;

import com.devsplan.ketchup.approval.dto.AppLineDTO;
import com.devsplan.ketchup.approval.dto.ApprovalDTO;
import com.devsplan.ketchup.approval.dto.ApprovalSelectDTO;
import com.devsplan.ketchup.approval.dto.RefLineDTO;

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
        ApprovalDTO approvalDTO = new ApprovalDTO(1, 1, "제목", "내용");
        AppLineDTO appLineDTO = new AppLineDTO(2, 1, "일반");
        AppLineDTO appLineDTO2 = new AppLineDTO(4, 2, "전결");
        RefLineDTO refLineDTO = new RefLineDTO(3);

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
        int memberNo = 1;
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
        int memberNo = 2;
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
        int memberNo = 3;
        String status = "완료";
        String searchValue = "";

        //then
        List<ApprovalSelectDTO> approvalSelectDTOList = service.selectRefApp(memberNo, status, searchValue);

        //when
        Assertions.assertNotNull(approvalSelectDTOList);
    }

    @Test
    public void testRepo(){
        List<Integer> num = appRepository.findAppNoByMemberNo(2);
        Assertions.assertTrue(num.size() == 1);
    }


}
