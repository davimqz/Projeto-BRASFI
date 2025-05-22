package Projeto_BRASFI.api_brasfi_backend.domain.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommunityDto(
        @NotBlank String name,
        String description,
        @NotNull Long chatId,
        @NotNull Long creatorId
) {}
