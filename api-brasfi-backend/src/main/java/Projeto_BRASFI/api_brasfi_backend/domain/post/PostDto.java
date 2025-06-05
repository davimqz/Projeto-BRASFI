package Projeto_BRASFI.api_brasfi_backend.domain.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {

    private Long id;

    @NotBlank
    private String content;

    private String mediaUrl;

    @NotNull
    private Long authorId;

    @NotNull
    private Long communityId;
}
