package com.devsplan.ketchup.approval;

import com.devsplan.ketchup.approval.controller.ApprovalController;
import com.devsplan.ketchup.approval.dto.AppLineDTO;
import com.devsplan.ketchup.approval.dto.ApprovalDTO;
import com.devsplan.ketchup.approval.dto.RefLineDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
public class ApprovalControllerTests {
    @Autowired
    ApprovalController approvalController;
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    public void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(approvalController).build();
    }
}
