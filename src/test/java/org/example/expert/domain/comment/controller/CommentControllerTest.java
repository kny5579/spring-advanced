package org.example.expert.domain.comment.controller;

import org.example.expert.config.JwtUtil;
import org.example.expert.domain.comment.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CommentService commentService;

    @Test
    void 댓글_목록_조회() throws Exception {
        // given
        long todoId = 1L;
        given(commentService.getComments(anyLong())).willReturn(List.of());

        // when
        mockMvc.perform(get("/todos/{todoId}/comments", todoId))
                .andExpect(status().isOk());
    }

}
