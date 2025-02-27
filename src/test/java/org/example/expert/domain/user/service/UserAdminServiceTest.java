package org.example.expert.domain.user.service;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserAdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAdminService userAdminService;

    @Test
    void UserRole을_변경할_수_있다() {
        // given
        long userId = 1L;
        User user = new User("email@email.com", "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);
        UserRoleChangeRequest request = new UserRoleChangeRequest("ADMIN");

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        userAdminService.changeUserRole(userId, request);

        //then
        assertEquals(UserRole.of(request.getRole()), user.getUserRole());
        verify(userRepository).findById(userId);
    }

    @Test
    void 존재하지_않는_user_조회_시_IRE() {
        // given
        long userId = 1L;
        UserRoleChangeRequest request = new UserRoleChangeRequest("ADMIN");

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(InvalidRequestException.class, () -> userAdminService.changeUserRole(userId, request));
        verify(userRepository).findById(userId);
    }
}
