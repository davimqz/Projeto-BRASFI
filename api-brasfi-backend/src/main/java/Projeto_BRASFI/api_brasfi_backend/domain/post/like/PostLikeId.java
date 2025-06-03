package Projeto_BRASFI.api_brasfi_backend.domain.post.like;

import java.io.Serializable;
import java.util.Objects;

public class PostLikeId implements Serializable {
    private Long memberId;
    private Long postId;

    public PostLikeId() {}

    public PostLikeId(Long memberId, Long postId) {
        this.memberId = memberId;
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostLikeId that)) return false;
        return Objects.equals(memberId, that.memberId) && Objects.equals(postId, that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, postId);
    }
}
