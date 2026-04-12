package com.agendafacil.api.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserSummaryDTO {
    private UUID id;
    private String name;
    private String email;
}

