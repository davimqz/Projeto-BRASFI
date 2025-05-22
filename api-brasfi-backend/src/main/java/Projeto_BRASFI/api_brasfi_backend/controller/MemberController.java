package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.member.*;
import Projeto_BRASFI.api_brasfi_backend.domain.member.block.MemberBlockService;
import Projeto_BRASFI.api_brasfi_backend.domain.member.follow.MemberFollowService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService service;
    private final MemberBlockService memberBlockService;
    private final MemberFollowService memberFollowService;

    public MemberController(MemberService service, MemberBlockService memberBlockService, MemberFollowService memberFollowService) {
        this.service = service;
        this.memberBlockService = memberBlockService;
        this.memberFollowService = memberFollowService;
    }


    @PostMapping
    public ResponseEntity<Member> create(@RequestBody @Valid MemberDto dto) {
        Member member = service.create(dto);
        return ResponseEntity.created(URI.create("/member/" + member.getId())).body(member);
    }

    @GetMapping
    public ResponseEntity<List<Member>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> update(@PathVariable Long id, @RequestBody @Valid MemberDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{memberId}/block/{blockedMemberId}")
    public ResponseEntity<Void> blockMember(@PathVariable Long memberId, @PathVariable Long blockedMemberId) {
        memberBlockService.blockMember(memberId, blockedMemberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{memberId}/unblock/{blockedMemberId}")
    public ResponseEntity<Void> unblockMember(@PathVariable Long memberId, @PathVariable Long blockedMemberId) {
        memberBlockService.unblockMember(memberId, blockedMemberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{followerId}/follow/{memberId}")
    public ResponseEntity<Void> followMember(@PathVariable Long followerId, @PathVariable Long memberId) {

        memberFollowService.followMember(memberId, followerId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{followerId}/unfollow/{memberId}")
    public ResponseEntity<Void> unfollowMember(@PathVariable Long followerId, @PathVariable Long memberId) {

        memberFollowService.unfollowMember(memberId, followerId);
        return ResponseEntity.noContent().build();
    }
}
