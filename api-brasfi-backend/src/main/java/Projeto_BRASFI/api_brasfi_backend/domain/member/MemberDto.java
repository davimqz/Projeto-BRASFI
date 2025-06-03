package Projeto_BRASFI.api_brasfi_backend.domain.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberDto(
        @NotBlank String name,
        @NotBlank String username,
        @Email String email,
        @NotBlank String passwordHash,
        String description
) {}
