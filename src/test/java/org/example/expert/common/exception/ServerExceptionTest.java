package org.example.expert.common.exception;

import org.example.expert.domain.common.exception.ServerException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServerExceptionTest {

    @Test
    void ServerException_발생() {
        // given
        String errorMessage = "ServerException";

        // when
        ServerException exception = assertThrows(ServerException.class, () -> {
            throw new ServerException(errorMessage);
        });

        // then
        assertEquals(errorMessage, exception.getMessage());
    }
}
