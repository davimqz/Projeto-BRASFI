package Projeto_BRASFI.api_brasfi_backend.domain.chat;


import jakarta.validation.constraints.NotBlank;

public record ChatDto(
        @NotBlank String type
) {}