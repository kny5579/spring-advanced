package org.example.expert.domain.Auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.auth.controller.AuthController;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void 회원가입_성공() throws Exception {
        // given
        SignupRequest signupRequest = new SignupRequest("email@email.com", "password", "USER");
        SignupResponse signupResponse = new SignupResponse("bearerToken");
        given(authService.signup(any(SignupRequest.class))).willReturn(signupResponse);

        // when&then
        mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bearerToken").value("bearerToken"));

    }

    @Test
    void 로그인_성공() throws Exception {
        // given
        SigninRequest signinRequest = new SigninRequest("email@email.com", "password");
        SigninResponse signinResponse = new SigninResponse("bearerToken");
        given(authService.signin(any(SigninRequest.class))).willReturn(signinResponse);

        // when&then
        mockMvc.perform(post("/auth/signin")
                        .content(objectMapper.writeValueAsString(signinRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bearerToken").value("bearerToken"));
    }

}
