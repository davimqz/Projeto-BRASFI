package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.post.*;
import Projeto_BRASFI.api_brasfi_backend.domain.post.like.PostLike;
import Projeto_BRASFI.api_brasfi_backend.domain.post.like.PostLikeDto;
import Projeto_BRASFI.api_brasfi_backend.domain.post.like.PostLikeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService service;
    private final PostLikeService likeService;

    public PostController(PostService service, PostLikeService likeService) {
        this.service = service;
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody @Valid PostDto dto) {
        Post created = service.create(dto);
        return ResponseEntity.created(URI.create("/post/" + created.getId())).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Post>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/community/{communityId}")
    public ResponseEntity<Page<Post>> getPostsByCommunity(
            @PathVariable Long communityId,
            Pageable pageable) {
        Page<Post> posts = service.findPostsByCommunity(communityId, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody @Valid PostDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like/{memberId}")
    public ResponseEntity<PostLike> like(@PathVariable Long postId, @PathVariable Long memberId) {
        PostLikeDto dto = new PostLikeDto(memberId, postId);
        return ResponseEntity.ok(likeService.like(dto));
    }

    @DeleteMapping("/{postId}/unlike/{memberId}")
    public ResponseEntity<Void> unlike(@PathVariable Long postId, @PathVariable Long memberId) {
        likeService.unlike(memberId, postId);
        return ResponseEntity.noContent().build();
    }


}
