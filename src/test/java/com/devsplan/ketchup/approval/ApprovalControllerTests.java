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

    private final String token = "Bearer eyJkYXRlIjoxNzE0NTQ0MzAzNDQwLCJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJwb3NpdGlvbk5hbWUiOiLtjIDsnqUiLCJkZXBObyI6NCwibWVtYmVyTm8iOiI0IiwicG9zaXRpb25MZXZlbCI6Miwic3ViIjoia2V0Y2h1cCB0b2tlbiA6IDQiLCJyb2xlIjoiTFYyIiwicG9zaXRpb25TdGF0dXMiOiJZIiwicG9zaXRpb25ObyI6MiwiZXhwIjoxNzE0NjMwNzAzfQ.Xzelrd6vLAhmr6ZdANihXUcKaQHCIlSYg0HZ5ntQhYA";
    private RequestBuilder request;

    @DisplayName("기안상신 컨트롤러 테스트")
    @Test
    public void insertAppTest() throws Exception{


        MockMultipartFile multipartFile = new MockMultipartFile("multipartFileList", "image.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        request = MockMvcRequestBuilders.multipart(HttpMethod.POST, "/approvals")
                .file(multipartFile)
                .param("approval.appMemberNo", "1")
                .param("approval.formNo", "1")
                .param("approval.appTitle","제목")
                .param("approval.appContents","내용")
                .param("appLineDTOList[0].alMemberNo", "2")
                .param("appLineDTOList[0].alType", "일반")
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
        int approvalNo = 2;
        String jsonbody = "{ \"action\": \"회수\", \"refusal\": \"\"}";

        request = MockMvcRequestBuilders.put("/approvals/" + approvalNo)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonbody)
                .header("Authorization", token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print());
    }



}
