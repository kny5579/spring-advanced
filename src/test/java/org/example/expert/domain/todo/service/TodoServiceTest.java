package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private WeatherClient weatherClient;

    @Mock
    private AuthUser authUser;

    @InjectMocks
    private TodoService todoService;

    @Test
    void TODO를_저장할_수_있다() {
        //given
        String weather = "sunny";
        TodoSaveRequest request = new TodoSaveRequest("title", "contents");
        Todo savedTodo = new Todo("title", "contents", weather, new User());

        given(weatherClient.getTodayWeather()).willReturn(weather);
        given(todoRepository.save(any(Todo.class))).willReturn(savedTodo);

        //when
        TodoSaveResponse todoSaveResponse = todoService.saveTodo(authUser, request);

        // then
        assertThat(todoSaveResponse).isNotNull();
        assertThat(todoSaveResponse.getTitle()).isEqualTo(request.getTitle());
        assertThat(todoSaveResponse.getContents()).isEqualTo(request.getContents());
    }

    @Test
    void TODO_저장_중_날씨_데이터_오류() {
        //given
        String weather = "sunny";
        TodoSaveRequest request = new TodoSaveRequest("title", "contents");
        Todo savedTodo = new Todo("title", "contents", weather, new User());

        given(weatherClient.getTodayWeather()).willReturn(weather);
        given(todoRepository.save(any(Todo.class))).willReturn(savedTodo);

        //when
        TodoSaveResponse todoSaveResponse = todoService.saveTodo(authUser, request);

        // then
        assertThat(todoSaveResponse).isNotNull();
        assertThat(todoSaveResponse.getTitle()).isEqualTo(request.getTitle());
        assertThat(todoSaveResponse.getContents()).isEqualTo(request.getContents());
    }

    @Test
    void TODO_목록을_페이징해서_조회할_수_있다() {
        //given
        int page = 2;
        int size = 2;
        Pageable pageable = PageRequest.of(page - 1, size);
        List<Todo> todoList = List.of(
                new Todo("title1", "contents1", "Sunny", new User()),
                new Todo("title2", "contents2", "Cloudy", new User()),
                new Todo("title3", "contents3", "Sunny", new User()),
                new Todo("title4", "contents4", "Rainy", new User())
        );

        List<Todo> pagedTodoList = todoList.subList((page - 1) * size, todoList.size());

        Page<Todo> todos = new PageImpl<>(pagedTodoList, pageable, todoList.size());

        given(todoRepository.findAllByOrderByModifiedAtDesc(pageable)).willReturn(todos);

        //when
        Page<TodoResponse> todoPages = todoService.getTodos(page, size);

        //then
        assertThat(todoPages).isNotNull();
        assertThat(todoPages.getContent().get(0).getTitle()).isEqualTo("title3");
        assertThat(todoPages.getContent().get(0).getContents()).isEqualTo("contents3");
        assertThat(todoPages.getContent().get(1).getTitle()).isEqualTo("title4");
        assertThat(todoPages.getContent().get(1).getContents()).isEqualTo("contents4");
    }

    @Test
    void TODO를_ID로_조회할_수_있다() {
        //given
        long todoId = 1L;
        Todo todo = new Todo("title1", "contents1", "Sunny", new User());
        ReflectionTestUtils.setField(todo, "id", todoId);

        given(todoRepository.findByIdWithUser(todoId)).willReturn(Optional.of(todo));

        //when
        TodoResponse todoResponse = todoService.getTodo(todoId);

        //then
        assertThat(todoResponse).isNotNull();
        assertThat(todoResponse.getId()).isEqualTo(todoId);
        assertThat(todoResponse.getTitle()).isEqualTo(todo.getTitle());
        assertThat(todoResponse.getContents()).isEqualTo(todo.getContents());
        assertThat(todoResponse.getCreatedAt()).isEqualTo(todo.getCreatedAt());
    }
}
