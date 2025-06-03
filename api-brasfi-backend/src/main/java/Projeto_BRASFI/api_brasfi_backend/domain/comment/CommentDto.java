package Projeto_BRASFI.api_brasfi_backend.domain.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentDto(
        @NotBlank String content,
        @NotNull Long memberId,
        @NotNull Long postId
) {}
