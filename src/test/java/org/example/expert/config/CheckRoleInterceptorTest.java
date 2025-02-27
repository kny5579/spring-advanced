package org.example.expert.config;

import org.example.expert.domain.common.exception.ForbiddenException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CheckRoleInterceptorTest {

    @InjectMocks
    private CheckRoleInterceptor checkRoleInterceptor;

    @Mock
    private Logger logger;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Test
    public void 사용자_권한이_request에_설정되지_않은_경우_예외_발생() throws Exception {
        // given
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.removeAttribute("userRole");

        // when&then
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            checkRoleInterceptor.preHandle(request, response, new Object());
        });

        assertEquals("사용자 정보가 없습니다.", exception.getMessage());
    }

    @Test
    public void 사용자_권한이_ADMIN이_아닌_경우_예외_발생() throws Exception {
        // given
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setAttribute("userRole", UserRole.USER);

        // when&then
        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> {
            checkRoleInterceptor.preHandle(request, response, new Object());
        });

        assertEquals("접근 권한이 없습니다.", exception.getMessage());
    }

    @Test
    public void 관리자_접근_성공() throws Exception {
        // given
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setAttribute("userRole", UserRole.ADMIN);

        // when
        boolean result = checkRoleInterceptor.preHandle(request, response, new Object());

        // then
        assertTrue(result);
    }
}
