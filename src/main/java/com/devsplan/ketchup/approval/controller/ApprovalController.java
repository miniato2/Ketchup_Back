package com.devsplan.ketchup.approval.controller;

import com.devsplan.ketchup.approval.dto.*;
import com.devsplan.ketchup.approval.service.ApprovalService;
import com.devsplan.ketchup.common.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.devsplan.ketchup.util.TokenUtils.decryptToken;

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
    /**
     * @api {post} /approvals 기안작성
     * @apiName insertApproval
     * @apiGroup Approval
     * @apiDescription 기안 작성 시 결재자 정보, 참조자 정보, 파일 등을 함께 전송합니다.
     *
     * @apiHeader {String} Authorization Bearer 토큰
     *
     * @apiParam {Number} approval.appMemberNo 기안자 회원 번호
     * @apiParam {Number} approval.formNo 기안 양식 번호
     * @apiParam {String} approval.appTitle 기안 제목
     * @apiParam {String} approval.appContents 기안 내용
     *
     * @apiParam {Object[]} appLineDTOList 결재 라인 정보
     * @apiParam {Number} appLineDTOList.alMemberNo 결재자 회원 번호
     * @apiParam {Number} appLineDTOList.alSequence 결재 순서
     *
     * @apiParam {Object[]} refLineDTOList 참조자 정보
     * @apiParam {Number} refLineDTOList.refMemberNo 참조자 회원 번호
     *
     * @apiParam {File[]} multipartFileList 첨부 파일 목록
     *
     * @apiSuccess {String} message 성공 메시지
     * @apiSuccess {Object} data 응답 데이터
     * @apiSuccess {Number} data.approvalId 생성된 기안 ID
     *
     * @apiError (400) {String} message 실패 메시지
     * @apiError (400) {Object} errors 에러 상세 정보
     */
    @PostMapping("/approvals")
    public ResponseEntity<ResponseDTO> insertApproval(@ModelAttribute AppInputDTO appInputDTO,
                                                      List<MultipartFile> multipartFileList){
        if (multipartFileList == null) {
            multipartFileList = Collections.emptyList();
        }

        if (appInputDTO.getRefLineDTOList() == null){
            appInputDTO.setRefLineDTOList(Collections.emptyList());
        }

        return ResponseEntity.ok().body(
                new ResponseDTO(HttpStatus.OK, "기안상신 완료",
                        approvalService.insertApproval(appInputDTO, multipartFileList))
        );
    }

    //기안 상세 조회
    /**
     * @api {get} /approvals/:approvalNo 기안 상세 조회
     * @apiName selectApprovalDetail
     * @apiGroup Approval
     * @apiDescription 기안 번호를 통해 기안 상세 정보를 조회합니다.
     *
     * @apiHeader {String} Authorization Bearer 토큰
     *
     * @apiParam {Number} approvalNo 기안 번호 (PathVariable)
     *
     * @apiSuccess {Number} approvalNo 기안 번호
     *
     * @apiSuccess {Object} member 기안자 정보
     * @apiSuccess {Number} member.memberNo 기안자 회원 번호
     * @apiSuccess {String} member.memberName 기안자 이름
     *
     * @apiSuccess {Object} form 양식 정보
     * @apiSuccess {Number} form.formNo 양식 번호
     * @apiSuccess {String} form.formTitle 양식 제목
     *
     * @apiSuccess {String} appTitle 기안 제목
     * @apiSuccess {String} appContents 기안 내용
     * @apiSuccess {String} appDate 기안 작성일
     * @apiSuccess {String} appFinalDate 최종 결재일
     * @apiSuccess {String} appStatus 기안 상태 (예: 진행 중, 결재 완료, 반려 등)
     * @apiSuccess {String} refusal 반려 사유 (있을 경우)
     * @apiSuccess {Number} sequence 결재 단계 번호
     *
     * @apiSuccess {Object[]} appFileList 첨부 파일 목록
     * @apiSuccess {String} appFileList.fileName 첨부 파일 이름
     * @apiSuccess {String} appFileList.fileSize 첨부 파일 크기
     *
     * @apiSuccess {Object[]} appLineList 결재 라인 정보
     * @apiSuccess {Number} appLineList.alMemberNo 결재자 회원 번호
     * @apiSuccess {String} appLineList.alMemberName 결재자 이름
     * @apiSuccess {Number} appLineList.alSequence 결재 순서
     * @apiSuccess {String} appLineList.alDate 결재일자
     *
     * @apiSuccess {Object[]} refLineList 참조 라인 정보
     * @apiSuccess {Number} refLineList.refMemberNo 참조자 회원 번호
     * @apiSuccess {String} refLineList.refMemberName 참조자 이름
     *
     * @apiError (404) {String} message 기안 정보를 찾을 수 없을 때의 에러 메시지
     * @apiError (404) {Object} errors 에러 상세 정보
     */
    @GetMapping("/approvals/{approvalNo}")
    public ResponseEntity<ResponseDTO> selectApprovalDetail(@PathVariable int approvalNo){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "성공", approvalService.selectApproval(approvalNo)));
    }


    
    //기안 목록 조회
    @GetMapping("/approvals")
    public ResponseEntity<ResponseDTO> selectApprovalList(@RequestParam String memberNo,
                                                          @RequestParam(defaultValue = "1") int category,
                                                          @RequestParam(defaultValue = "전체") String status,
                                                          @RequestParam(defaultValue = "") String search,
                                                          @RequestParam(name = "page", defaultValue = "1")String offset){

        Criteria cri = new Criteria(Integer.valueOf(offset), 10);
        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();

        Page<ApprovalSelectDTO> appList = null;

        List<String> statusList = new ArrayList<>();

        // 검색 결과
        switch (category){
            case 1:
                if(status.equals("전체")){
                    statusList.add("대기");
                    statusList.add("진행");
                }else {
                    statusList.add(status);
                }
                appList = approvalService.selectMyApproval(memberNo, statusList, search, cri);
                break;
            case 2:
                if(status.equals("전체")){
                    statusList.add("완료");
                    statusList.add("반려");
                    statusList.add("회수");
                }else {
                    statusList.add(status);
                }
                appList = approvalService.selectMyApproval(memberNo, statusList, search, cri);
                break;
            case 3:
                if(status.equals("전체")){
                    statusList.add("대기");
                    statusList.add("진행");
                }else{
                    statusList.add(status);
                }
                appList = approvalService.selectReceiveApp(memberNo, statusList, search, cri);
                break;
            case 4:
                if(status.equals("전체")){
                    statusList.add("대기");
                    statusList.add("진행");
                    statusList.add("완료");
                    statusList.add("반려");
                }else{
                    statusList.add(status);
                }
                appList = approvalService.selectRefApp(memberNo, statusList, search, cri);
                break;
        }

        pagingResponseDTO.setData(appList);
        pagingResponseDTO.setPageInfo(new PageDTO(cri, (int) appList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", pagingResponseDTO));
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
                                                       @RequestBody AppUpdateDTO appUpdateDTO,
                                                       @RequestHeader("Authorization") String token){
        String result = "";
        String memberNo = decryptToken(token).get("memberNo", String.class); //사원 번호 받아옮

        if(appUpdateDTO.getAction().equals("회수")){
            result = approvalService.updateApproval(approvalNo);
        }else{
            result = approvalService.updateApproval2(appUpdateDTO, memberNo, approvalNo);
        }
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "성공", result));
    }

    //기안 카테고리 개수 조회
    @GetMapping("/approvals/count")
    public ResponseEntity<ResponseDTO> selectApprovalsCount(@RequestParam String memberNo){

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "성공", approvalService.selectApprovalCount(memberNo)));
    }


}
