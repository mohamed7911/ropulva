package com.ropulva.calendar.business.layer.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpRequestDTO {
    @NotEmpty(message = "Username is mandatory")
    private String username;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;
}
