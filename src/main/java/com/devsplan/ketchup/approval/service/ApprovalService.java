package com.devsplan.ketchup.approval.service;

import com.devsplan.ketchup.approval.dto.AppFileDTO;
import com.devsplan.ketchup.approval.dto.AppLineDTO;
import com.devsplan.ketchup.approval.dto.ApprovalDTO;
import com.devsplan.ketchup.approval.dto.RefLineDTO;
import com.devsplan.ketchup.approval.entity.AppFile;
import com.devsplan.ketchup.approval.entity.AppLine;
import com.devsplan.ketchup.approval.entity.Approval;
import com.devsplan.ketchup.approval.entity.RefLine;
import com.devsplan.ketchup.approval.repository.AppFileRepository;
import com.devsplan.ketchup.approval.repository.AppLineRepository;
import com.devsplan.ketchup.approval.repository.ApprovalRepository;
import com.devsplan.ketchup.approval.repository.RefLineRepository;
import com.devsplan.ketchup.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final AppLineRepository appLineRepository;
    private final RefLineRepository refLineRepository;
    private final AppFileRepository appFileRepository;
    private final ModelMapper modelMapper;

    @Value("${image.image-dir}")
    private String IMAGE_DIR;
    @Value("${image.image-url}")
    private String IMAGE_URL;

    @Autowired
    public ApprovalService(ApprovalRepository approvalRepository,
                           AppLineRepository appLineRepository,
                           RefLineRepository refLineRepository,
                           AppFileRepository appFileRepository,
                           ModelMapper modelMapper) {

        this.approvalRepository = approvalRepository;
        this.appLineRepository = appLineRepository;
        this.refLineRepository = refLineRepository;
        this.appFileRepository = appFileRepository;
        this.modelMapper = modelMapper;
    }

    //기안 등록
    @Transactional
    public Object insertApproval(ApprovalDTO approvalDTO,
                                 List<AppLineDTO> appLineDTOList,
                                 List<RefLineDTO> refLineDTOList,
                                 List<MultipartFile> multipartFileList) {
        int result = 0;

        //기안 count + 1 => 현재 등록하려는 기안 번호 세팅
        int approvalNo = (int) approvalRepository.count() + 1;
        List<AppFileDTO> fileDTOList = new ArrayList<>();

        //파일 저장
        try {
            if (multipartFileList != null || !multipartFileList.isEmpty()) {

                for (MultipartFile multipartFile : multipartFileList) {
                    String fileName = UUID.randomUUID().toString().replace("-", "");
                    String replaceFileName = null;

                    replaceFileName = FileUtils.saveFile(IMAGE_DIR, fileName, multipartFile);

                    AppFileDTO appFileDTO = new AppFileDTO(approvalNo, replaceFileName);
                    fileDTOList.add(appFileDTO);
                }
                List<AppFile> appFileList = new ArrayList<>();
                //파일
                fileDTOList.forEach(file -> {
                    AppFile appFile = modelMapper.map(file, AppFile.class);
                    appFileList.add(appFile);
                });
                appFileRepository.saveAll(appFileList);
            }

            List<AppLine> appLineList = new ArrayList<>();
            List<RefLine> refLineList = new ArrayList<>();

            //결재선
            appLineDTOList.forEach(al -> {
                al.setApprovalNo(approvalNo);
                AppLine appLine = modelMapper.map(al, AppLine.class);
                appLineList.add(appLine);
            });

            //참조선
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

            Approval approval = modelMapper.map(approvalDTO, Approval.class);

            approvalRepository.save(approval);
            appLineRepository.saveAll(appLineList);
            refLineRepository.saveAll(refLineList);

            result = 1;

        } catch (IOException e) {
            fileDTOList.forEach(file -> FileUtils.deleteFile(IMAGE_DIR, file.getFileUrl()));
            throw new RuntimeException(e);
        }

        return (result > 0) ? "성공" : "실패";
    }


    public List<ApprovalDTO> selectMyApproval(int memberNo, int category, String status, String searchValue) {

        return null;
    }
}
