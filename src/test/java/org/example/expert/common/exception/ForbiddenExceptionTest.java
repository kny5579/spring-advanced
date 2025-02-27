package org.example.expert.common.exception;

import org.example.expert.domain.common.exception.ForbiddenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ForbiddenExceptionTest {

    @Test
    void ForbiddenException_발생() {
        // given
        String errorMessage = "ForbiddenException";

        // when
        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> {
            throw new ForbiddenException(errorMessage);
        });

        // then
        assertEquals(errorMessage, exception.getMessage());
    }
}
