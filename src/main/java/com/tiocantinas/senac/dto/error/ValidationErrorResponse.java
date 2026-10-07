package com.tiocantinas.senac.dto.error;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        int status,
        String erro,
        String mensagem,
        Map<String, String> campos,
        LocalDateTime timestamp) {
}
