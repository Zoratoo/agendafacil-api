package com.agendafacil.api.modules.auth.dto;

import com.agendafacil.api.modules.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String role;
}
