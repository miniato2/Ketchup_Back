package com.devsplan.ketchup.approval;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ApprovalControllerTests {

    @Autowired
    private MockMvc mvc;

    private final String token = "Bearer eyJkYXRlIjoxNzE0NjM0NzA0NTY0LCJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJwb3NpdGlvbk5hbWUiOiLsp4Hsm5AiLCJkZXBObyI6MSwibWVtYmVyTm8iOiIxIiwicG9zaXRpb25MZXZlbCI6MSwic3ViIjoia2V0Y2h1cCB0b2tlbiA6IDEiLCJyb2xlIjoiTFYxIiwicG9zaXRpb25TdGF0dXMiOiJZIiwicG9zaXRpb25ObyI6MSwiZXhwIjoxNzE0NzIxMTA0fQ.23zT3c_XKulnPybkM83th2M1j7pvSARmRkkRg3B7yeA";
    private RequestBuilder request;

    @DisplayName("기안상신 컨트롤러 테스트")
    @Test
    public void insertAppTest() throws Exception{


        MockMultipartFile multipartFile = new MockMultipartFile("multipartFileList", "image.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        request = MockMvcRequestBuilders.multipart(HttpMethod.POST, "/approvals")
                .file(multipartFile)
                .param("approval.appMemberNo", "4")
                .param("approval.formNo", "1")
                .param("approval.appTitle","제목")
                .param("approval.appContents","내용")
                .param("appLineDTOList[0].alMemberNo", "2")
                .param("appLineDTOList[0].alType", "일반")
                .param("appLineDTOList[0].alSequence", "1")
                .param("refLineDTOList[0].refMemberNo", "3")
                .header("Authorization", token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print());

    }

    @DisplayName("양식 조회 컨트롤러 테스트")
    @Test
    public void selectFormTest() throws Exception {
        int formNo = 1;

        request = MockMvcRequestBuilders.get("/forms/"+formNo)
                .header("Authorization", token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("기안상태 수정 컨트롤러 테스트")
    @Test
    public void updateApprovalTest() throws Exception {
        int approvalNo = 1;
        String jsonbody = "{ \"action\": \"반려\", \"refusal\": \"그냥\"}";

        request = MockMvcRequestBuilders.put("/approvals/" + approvalNo)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonbody)
                .header("Authorization", token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("기안 상세 조회 테스트")
    @Test
    public void selectApprovalTest() throws Exception {
        int approvalNo = 1;
        request = MockMvcRequestBuilders.get("/approvals/" + approvalNo)
                .header("Authorization", token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("기안 목록 조회 테스트 카테고리 1")
    @Test
    public void selectApprovalListTest() throws Exception {
        String memberNo = "4";
        int categoryNo = 1;
        String status = "전체";
        String search = "제";

        request = MockMvcRequestBuilders.get("/approvals?memberNo=" + memberNo +
                "&category=" + categoryNo + "&status=" + status + "&search=" + search)
                .header("Authorization", token);
        mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print());

    }

    @DisplayName("기안 목록 조회 테스트 카테고리 3")
    @Test
    public void selectApprovalListTest2() throws Exception {
        String memberNo = "2";
        int categoryNo = 3;
        String status = "전체";
        String search = "제";

        request = MockMvcRequestBuilders.get("/approvals?memberNo=" + memberNo +
                        "&category=" + categoryNo + "&status=" + status + "&search=" + search)
                .header("Authorization", token);
        mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print());

    }
    @DisplayName("기안 목록 조회 테스트 카테고리 4")
    @Test
    public void selectApprovalListTest3() throws Exception {
        String memberNo = "3";
        int categoryNo = 4;
        String status = "전체";
        String search = "제";

        request = MockMvcRequestBuilders.get("/approvals?memberNo=" + memberNo +
                        "&category=" + categoryNo + "&status=" + status + "&search=" + search)
                .header("Authorization", token);
        mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print());

    }



}
