package Projeto_BRASFI.api_brasfi_backend.domain.post.like;

import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import Projeto_BRASFI.api_brasfi_backend.domain.member.MemberRepository;
import Projeto_BRASFI.api_brasfi_backend.domain.post.Post;
import Projeto_BRASFI.api_brasfi_backend.domain.post.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostLikeService {

    private final PostLikeRepository repository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostLikeService(PostLikeRepository repository, PostRepository postRepository, MemberRepository memberRepository) {
        this.repository = repository;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public PostLike like(PostLikeDto dto) {
        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Member member = memberRepository.findById(dto.memberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        PostLikeId id = new PostLikeId(dto.memberId(), dto.postId());
        if (repository.existsById(id)) {
            throw new IllegalStateException("Post already liked by this member");
        }

        PostLike like = new PostLike(member, post);
        return repository.save(like);
    }


    @Transactional
    public void unlike(Long memberId, Long postId) {
        PostLikeId id = new PostLikeId(memberId, postId);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Like not found");
        }
        repository.deleteById(id);
    }
}
