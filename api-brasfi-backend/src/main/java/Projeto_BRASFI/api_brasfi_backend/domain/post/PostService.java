package Projeto_BRASFI.api_brasfi_backend.domain.post;

import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import Projeto_BRASFI.api_brasfi_backend.domain.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostService(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    public List<Post> listAll() {
        return postRepository.findAll();
    }

    @Transactional
    public Post create(PostDto dto) {
        Member author = memberRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        Post post = new Post();
        post.setContent(dto.getContent());
        post.setMediaUrl(dto.getMediaUrl());
        post.setCreatedAt(LocalDateTime.now());
        post.setAuthor(author);
        return postRepository.save(post);
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    @Transactional
    public Post update(Long id, PostDto dto) {
        Post post = findById(id);
        post.setContent(dto.getContent());
        post.setMediaUrl(dto.getMediaUrl());
        return post;
    }

    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
