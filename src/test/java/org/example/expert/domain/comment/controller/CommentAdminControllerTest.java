package org.example.expert.domain.comment.controller;

import org.example.expert.config.JwtUtil;
import org.example.expert.domain.comment.service.CommentAdminService;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentAdminController.class)
public class CommentAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CommentAdminService commentAdminService;

    @Test
    void 댓글_삭제() throws Exception {
        //given
        long commentId = 1L;

        doNothing().when(commentAdminService).deleteComment(commentId);

        //when&then
        mockMvc.perform(delete("/admin/comments/{commentId}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userRole", UserRole.ADMIN))
                .andExpect(status().isOk());

    }
}
