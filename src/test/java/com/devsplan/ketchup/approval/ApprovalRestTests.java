package com.devsplan.ketchup.approval;

import com.devsplan.ketchup.approval.dto.AppLineDTO;
import com.devsplan.ketchup.approval.dto.ApprovalDTO;
import com.devsplan.ketchup.approval.dto.RefLineDTO;
import com.devsplan.ketchup.approval.repository.ApprovalRepository;
import com.devsplan.ketchup.approval.service.ApprovalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ApprovalRestTests {
    @Autowired
    private ApprovalService service;
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

    @Test
    public void selectMyApprovalTest(){
        //given
        int memberNo = 1;
        int category = 1;
        String status = "대기";
        String searchValue = "";

        //when
        List<ApprovalDTO> approvalDTOList = service.selectMyApproval(memberNo, category, status, searchValue);

        //then
        Assertions.assertNotNull(approvalDTOList);
    }
}
