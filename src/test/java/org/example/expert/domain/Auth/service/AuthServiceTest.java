package org.example.expert.domain.Auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.auth.service.AuthService;
import org.example.expert.domain.common.exception.InvalidRequestException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void 회원가입_정상_등록() {
        //given
        long userId = 1L;
        SignupRequest signupRequest = new SignupRequest("aa@aa.com", "password", "USER");

        given(userRepository.existsByEmail(signupRequest.getEmail())).willReturn(false);
        given(passwordEncoder.encode(signupRequest.getPassword())).willReturn("encodedPassword");

        UserRole userRole = UserRole.of(signupRequest.getUserRole());
        User newUser = new User(signupRequest.getEmail(), "encodedPassword", userRole);
        ReflectionTestUtils.setField(newUser, "id", userId);

        given(userRepository.save(any(User.class))).willReturn(newUser);
        given(jwtUtil.createToken(anyLong(), anyString(), any(UserRole.class))).willReturn("BearerToken");

        // when
        SignupResponse signupResponse = authService.signup(signupRequest);

        // then
        assertNotNull(signupResponse);
        assertEquals("BearerToken", signupResponse.getBearerToken());
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    void 회원가입_실패() {
        // given
        long userId = 1L;
        SignupRequest signupRequest = new SignupRequest("aa@aa.com", "password", "USER");

        given(userRepository.existsByEmail(signupRequest.getEmail())).willReturn(true);

        // when&then
        assertThrows(InvalidRequestException.class, () -> authService.signup(signupRequest),
                "이미 존재하는 이메일입니다.");

    }

    @Test
    void 로그인_성공() {
        // given
        SigninRequest signinRequest = new SigninRequest("email", "password");
        User user = new User("email", "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

        String bearerToken = "bearerToken";

        given(jwtUtil.createToken(anyLong(), anyString(), any(UserRole.class))).willReturn(bearerToken);

        // when
        SigninResponse signinResponse = authService.signin(signinRequest);

        // then
        assertNotNull(signinResponse);
        assertEquals("bearerToken", signinResponse.getBearerToken());
    }

    @Test
    void 로그인_실패_가입되지_않은_유저() {
        // given
        SigninRequest signinRequest = new SigninRequest("email", "password");
        User user = new User("email", "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when&then
        assertThrows(InvalidRequestException.class, () -> authService.signin(signinRequest),
                "가입되지 않은 유저입니다.");
    }

    @Test
    void 로그인_실패_잘못된_비밀번호() {
        // given
        SigninRequest signinRequest = new SigninRequest("email", "password");
        User user = new User("email", "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when&then
        assertThrows(AuthException.class, () -> authService.signin(signinRequest),
                "잘못된 비밀번호입니다.");

    }

}
