package com.agendafacil.api.shared.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponseDTO {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}
