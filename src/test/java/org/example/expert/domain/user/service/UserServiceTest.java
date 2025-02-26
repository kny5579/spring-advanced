package org.example.expert.domain.user.service;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void User를_ID로_조회할_수_있다() {
        // given
        String email = "email@email.com";
        long userId = 1L;
        User user = new User(email, "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        UserResponse userResponse = userService.getUser(userId);

        // then
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isEqualTo(userId);
        assertThat(userResponse.getEmail()).isEqualTo(email);
    }

    @Test
    void 사용자의_비밀번호를_변경할_수_있다() {
        // given
        long userId = 1L;
        User user = new User("email@email.com", "oldPassword", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);
        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword","newPassword1");

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        given(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).willReturn(false);
        given(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.encode(request.getNewPassword())).willReturn("encodedPassword");

        // when
        userService.changePassword(userId, request);

        //then
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository).findById(userId);
        verify(passwordEncoder).encode(request.getNewPassword());

    }

    @Test
    void 새_비밀번호가_기존_비밀번호와_같을_경우_예외가_발생한다() {
        // given
        long userId = 1L;
        User user = new User("email@email.com", "oldPassword", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword", "oldPassword");

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).willReturn(true);

        // when, then
        assertThrows(InvalidRequestException.class, () -> userService.changePassword(userId, request),
                "새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
    }

    @Test
    void 잘못된_비밀번호가_입력되었을_경우_예외가_발생한다() {
        // given
        long userId = 1L;
        User user = new User("email@email.com", "oldPassword", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        UserChangePasswordRequest request = new UserChangePasswordRequest("notOldPassword", "newPassword");

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).willReturn(false);
        given(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).willReturn(false);

        // when, then
        assertThrows(InvalidRequestException.class, () -> userService.changePassword(userId, request),
                "잘못된 비밀번호입니다.");
    }

}
