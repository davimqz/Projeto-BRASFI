package Projeto_BRASFI.api_brasfi_backend.domain.post.like;

import jakarta.validation.constraints.NotNull;

public record PostLikeDto(
        @NotNull Long memberId,
        @NotNull Long postId
) {}
