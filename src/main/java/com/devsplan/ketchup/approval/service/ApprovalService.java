package com.devsplan.ketchup.approval.service;

import com.devsplan.ketchup.approval.dto.AppLineDTO;
import com.devsplan.ketchup.approval.dto.ApprovalDTO;
import com.devsplan.ketchup.approval.dto.RefLineDTO;
import com.devsplan.ketchup.approval.entity.AppLine;
import com.devsplan.ketchup.approval.entity.Approval;
import com.devsplan.ketchup.approval.entity.RefLine;
import com.devsplan.ketchup.approval.repository.AppLineRepository;
import com.devsplan.ketchup.approval.repository.ApprovalRepository;
import com.devsplan.ketchup.approval.repository.RefLineRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final AppLineRepository appLineRepository;
    private final RefLineRepository refLineRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ApprovalService(ApprovalRepository approvalRepository,
                           AppLineRepository appLineRepository,
                           RefLineRepository refLineRepository,
                           ModelMapper modelMapper){

        this.approvalRepository = approvalRepository;
        this.appLineRepository = appLineRepository;
        this.refLineRepository = refLineRepository;
        this.modelMapper = modelMapper;
    }

    //등록
    @Transactional
    public Object insertApproval(ApprovalDTO approvalDTO,
                                 List<AppLineDTO> appLineDTOList,
                                 List<RefLineDTO> refLineDTOList){
        int result = 0;

        try{
            //기안 count + 1 => 현재 등록하려는 기안 번호 세팅
            int approvalNo = (int) approvalRepository.count() + 1;

            List<AppLine> appLineList = new ArrayList<>();
            List<RefLine> refLineList = new ArrayList<>();

            appLineDTOList.forEach(al -> {
                al.setApprovalNo(approvalNo);
                AppLine appLine = modelMapper.map(al, AppLine.class);
                appLineList.add(appLine);
            });

            refLineDTOList.forEach(rl -> {
                rl.setApprovalNo(approvalNo);
                RefLine refLine = modelMapper.map(rl, RefLine.class);
                refLineList.add(refLine);
            });

            approvalDTO.setApprovalNo(approvalNo);

            java.util.Date now = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            String appDate = sdf.format(now);

            approvalDTO.setAppDate(appDate);
            approvalDTO.setAppStatus("대기");

            //파일 업로드 로직 필요

            Approval approval = modelMapper.map(approvalDTO, Approval.class);

            approvalRepository.save(approval);
            appLineRepository.saveAll(appLineList);
            refLineRepository.saveAll(refLineList);

            result = 1;
        }catch (Exception e){
            e.printStackTrace();

            //파일 삭제
        }
        return (result > 0) ? "성공" : "실패";
    }

    public List<ApprovalDTO> selectMyApproval(int memberNo, int category, String status, String searchValue) {

        return null;
    }
}
