package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.ForbiddenException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.enums.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class CheckRoleInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(CheckRoleInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        LocalDateTime requestTime = LocalDateTime.now();

        UserRole userRole = (UserRole) request.getAttribute("userRole");

        if (userRole == null) {
            logger.warn("userRole 정보가 request에 존재하지 않습니다.");
            throw new InvalidRequestException("사용자 정보가 없습니다.");
        }

        if (userRole != UserRole.ADMIN) {
            logger.warn("접근 권한이 없습니다. URI:{}",request.getRequestURI());
            throw new ForbiddenException("접근 권한이 없습니다.");
        }

        logger.info("관리자로 접근되었습니다. URL:{} time:{}", request.getRequestURI(), requestTime);
        return true;

    }

}
