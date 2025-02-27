package org.example.expert.domain.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.AuthUserArgumentResolver;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    @MockBean
    private AuthUserArgumentResolver authUserArgumentResolver;

    @InjectMocks
    private TodoController todoController;

    @Test
    void TODO_목록을_페이징해서_조회한다() throws Exception {
        //given
        int page = 1;
        int size = 2;
        LocalDateTime time = LocalDateTime.now();
        Pageable pageable = PageRequest.of(page - 1, size);
        List<TodoResponse> todoList = List.of(
                new TodoResponse(1L, "t1", "c1", "Sunny", new UserResponse(new User()), time, time),
                new TodoResponse(2L, "t2", "c2", "Sunny", new UserResponse(new User()), time, time),
                new TodoResponse(3L, "t3", "c3", "Sunny", new UserResponse(new User()), time, time),
                new TodoResponse(4L, "t4", "c4", "Sunny", new UserResponse(new User()), time, time)
        );

        List<TodoResponse> pagedTodoList = todoList.subList((page - 1) * size, todoList.size());

        Page<TodoResponse> todos = new PageImpl<>(pagedTodoList, pageable, todoList.size());
        given(todoService.getTodos(page, size)).willReturn(todos);

        //when&then
        mockMvc.perform(get("/todos")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("t1"))
                .andExpect(jsonPath("$.content[1].title").value("t2"))
                .andExpect(jsonPath("$.content[0].contents").value("c1"))
                .andExpect(jsonPath("$.content[1].contents").value("c2"));
    }

    @Test
    void TODO를_ID로_조회한다() throws Exception {
        //given
        long todoId = 1L;
        Todo todo = new Todo("title1", "contents1", "Sunny", new User());
        ReflectionTestUtils.setField(todo, "id", todoId);
        given(todoService.getTodo(todoId)).willReturn(new TodoResponse(todo));

        //when&then
        mockMvc.perform(get("/todos/{todoId}", todoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId))
                .andExpect(jsonPath("$.title").value(todo.getTitle()))
                .andExpect(jsonPath("$.contents").value(todo.getContents()))
                .andExpect(jsonPath("$.weather").value(todo.getWeather()))
                .andExpect(jsonPath("$.createdAt").value(todo.getCreatedAt()))
                .andExpect(jsonPath("$.modifiedAt").value(todo.getModifiedAt()));
    }

}
