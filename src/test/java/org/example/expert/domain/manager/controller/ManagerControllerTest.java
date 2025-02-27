package org.example.expert.domain.manager.controller;

import org.example.expert.config.AuthUserArgumentResolver;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.manager.service.ManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagerController.class)
public class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private ManagerService managerService;

    @MockBean
    private AuthUserArgumentResolver authUserArgumentResolver;

    @Test
    void 매니저_전체_검색() throws Exception {
        // given
        long todoId = 1L;
        given(managerService.getManagers(todoId)).willReturn(List.of());

        // when&then
        mockMvc.perform(get("/todos/{todoId}/managers", todoId))
                .andExpect(status().isOk());
    }
}
