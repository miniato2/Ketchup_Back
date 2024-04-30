package com.devsplan.ketchup.approval.service;

import com.devsplan.ketchup.approval.dto.*;
import com.devsplan.ketchup.approval.entity.*;
import com.devsplan.ketchup.approval.repository.*;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final AppLineRepository appLineRepository;
    private final RefLineRepository refLineRepository;
    private final AppFileRepository appFileRepository;
    private final ApprovalSelectRepository approvalSelectRepository;
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
                           ApprovalSelectRepository approvalSelectRepository,
                           ModelMapper modelMapper) {

        this.approvalRepository = approvalRepository;
        this.appLineRepository = appLineRepository;
        this.refLineRepository = refLineRepository;
        this.appFileRepository = appFileRepository;
        this.approvalSelectRepository = approvalSelectRepository;
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



    //내가 작성한 기안 조회
    @Transactional
    public List<ApprovalSelectDTO> selectMyApproval(String memberNo, List<String> status, String searchValue) {
//        Pageable paging = PageRequest.of(index, count, Sort.by("productCode").descending());
        List<ApprovalSelect> approvalSelectList = null;

        if(searchValue == null){
            approvalSelectList = approvalSelectRepository.findMyApproval(memberNo, status);
        }else{
            approvalSelectList = approvalSelectRepository.findMyApprovalWithSearch(memberNo, status, searchValue);
        }

        List<ApprovalSelectDTO> approvalSelectDTOList = null;

        approvalSelectDTOList = approvalSelectList.stream().map(approvalAnd ->
                        modelMapper.map(approvalAnd, ApprovalSelectDTO.class))
                        .collect(Collectors.toList());

        log.info("List ===========================================");

        return approvalSelectDTOList;
    }

    //받은 기안 조회
    @Transactional
    public List<ApprovalSelectDTO> selectReceiveApp(String memberNo, List<String> status, String searchValue){

        List<Integer> appNo = appLineRepository.findAppNoByMemberNo(memberNo);
        List<ApprovalSelect> approvalSelectList = null;

        if(searchValue == null){
            approvalSelectList = approvalSelectRepository.findReceiveApp(status, appNo);
        }else{
            approvalSelectList = approvalSelectRepository.findReceiveAppWithSearch(status, appNo, searchValue);
        }
        List<ApprovalSelectDTO> approvalSelectDTOList = approvalSelectList.stream().map(approvalAnd ->
                        modelMapper.map(approvalAnd, ApprovalSelectDTO.class))
                .collect(Collectors.toList());

        return approvalSelectDTOList;
    }

    //참조선으로 등록된 기안 조회
    public List<ApprovalSelectDTO> selectRefApp(String memberNo, String status, String searchValue) {
        List<ApprovalSelect> approvalSelectList = null;
        List<Integer> appNo = refLineRepository.findAppNoByMemberNo(memberNo); //사원번호로 참조선에 등록된 기안번호를 조회

        if(searchValue == null){
            approvalSelectList = approvalSelectRepository.findRefApp(status, appNo);
        }else{
            approvalSelectList = approvalSelectRepository.findRefAppWithSearch(status, searchValue, appNo);
        }

        List<ApprovalSelectDTO> approvalSelectDTOList = approvalSelectList.stream().map(approvalAnd ->
                        modelMapper.map(approvalAnd, ApprovalSelectDTO.class))
                .collect(Collectors.toList());

        return approvalSelectDTOList;
    }


    //기안 상세 조회
    @Transactional
    public ApprovalSelectDTO selectApproval(int appNo) {
        ApprovalSelect approvalSelect = approvalSelectRepository.findById(appNo).get();
        ApprovalSelectDTO approvalSelectDTO = modelMapper.map(approvalSelect, ApprovalSelectDTO.class);

        return approvalSelectDTO;
    }

    //기안 회수
    @Transactional
    public String updateApproval(AppUpdateDTO appUpdateDTO) {
        int result = 0;

        Approval approval = approvalRepository.findById(appUpdateDTO.getApprovalNo()).get();

        if(approval.getAppStatus().equals("대기")){
            approval = approval.appStatus("회수").build();
            result = 1;
        }
        return (result > 0) ? "성공" : "실패";
    }

    //기안 처리 (결재 반려)
    @Transactional
    public String updateApproval2(AppUpdateDTO appUpdateDTO, String memberNo){
        int result = 0;
        Approval approval = approvalRepository.findById(appUpdateDTO.getApprovalNo()).get();
        AppLine appLine = appLineRepository.findByMemberNoAndApprovalNo(memberNo, appUpdateDTO.getApprovalNo());

        java.util.Date now = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        String appDate = sdf.format(now);

        switch(appUpdateDTO.getAction()){
            case "결재" :
                int count = appLineRepository.countSequence(appUpdateDTO.getApprovalNo());
                if(appLine.getAlSequence() == count){
                    approval.appStatus("완료").appFinalDate(appDate).build();
                    appLine = appLine.alDate(appDate).build();
                    result = 1;
                }else{
                    approval.appStatus("진행").appFinalDate(appDate).build();
                    appLine = appLine.alDate(appDate).build();
                    result = 1;
                }
                break;
            case "반려" :
                approval = approval.appStatus("반려").refusal(appUpdateDTO.getRefusal()).build();
                appLine = appLine.alDate(appDate).build();
                result = 1;
                break;
            case "전결" :
                approval = approval.appStatus("완료").appFinalDate(appDate).build();
                appLine = appLine.alDate(appDate).build();
                result = 1;
                break;
            default :
                break;
        }

        return (result > 0) ? "성공" : "실패";
    }



}
