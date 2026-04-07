package com.agendafacil.api.modules.auth.dto;

import com.agendafacil.api.modules.user.entity.Role;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String name;
    private String email;
    private String password;
    private Role role;
}
