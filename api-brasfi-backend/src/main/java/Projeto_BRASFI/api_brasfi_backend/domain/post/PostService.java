package Projeto_BRASFI.api_brasfi_backend.domain.post;

import Projeto_BRASFI.api_brasfi_backend.domain.community.Community;
import Projeto_BRASFI.api_brasfi_backend.domain.community.CommunityService;
import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import Projeto_BRASFI.api_brasfi_backend.domain.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommunityService communityService;

    public PostService(PostRepository postRepository, MemberRepository memberRepository, CommunityService communityService) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.communityService = communityService;
    }

    public List<Post> listAll() {
        return postRepository.findAll();
    }

    @Transactional
    public Post create(PostDto dto) {
        Member author = memberRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with ID: " + dto.getAuthorId()));
        
        Community community = communityService.findById(dto.getCommunityId());

        Post post = new Post();
        post.setContent(dto.getContent());
        post.setMediaUrl(dto.getMediaUrl());
        post.setCreatedAt(LocalDateTime.now());
        post.setAuthor(author);
        post.setCommunity(community);
        return postRepository.save(post);
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + id));
    }

    @Transactional
    public Post update(Long id, PostDto dto) {
        Post post = findById(id);
        
        if (dto.getContent() != null) {
            post.setContent(dto.getContent());
        }
        if (dto.getMediaUrl() != null) {
            if (dto.getMediaUrl().trim().isEmpty()) {
                post.setMediaUrl(null);
            } else {
                post.setMediaUrl(dto.getMediaUrl());
            }
        } else {
            post.setMediaUrl(null);
        }

        post.setCreatedAt(LocalDateTime.now());
        
        return postRepository.save(post);
    }

    @Transactional
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new EntityNotFoundException("Post not found with ID: " + id);
        }
        postRepository.deleteById(id);
    }

    public Page<Post> findPostsByCommunity(Long communityId, Pageable pageable) {
        communityService.findById(communityId);
        return postRepository.findByCommunityIdOrderByCreatedAtDesc(communityId, pageable);
    }
}
