package dev.jaoow.financeapp.model.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class UserResponseDTO {
    private Long id;
    private String email;
    private Date createdAt;
    private Date updatedAt;
    private Set<String> roles;
}
