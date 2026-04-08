package com.agendafacil.api.modules.establishment.dto;

import com.agendafacil.api.modules.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateEstablishmentDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String category;
    private String phone;
    private String cep;
    private String address;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
}
