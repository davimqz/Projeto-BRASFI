package Projeto_BRASFI.api_brasfi_backend.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByCommunityIdOrderByCreatedAtDesc(Long communityId, Pageable pageable);
}
