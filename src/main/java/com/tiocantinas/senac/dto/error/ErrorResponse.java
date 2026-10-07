package com.tiocantinas.senac.dto.error;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String erro,
        String mensagem,
        LocalDateTime timestamp) {
}