package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.member.*;
import Projeto_BRASFI.api_brasfi_backend.domain.member.block.MemberBlockService;
import Projeto_BRASFI.api_brasfi_backend.domain.member.community.CommunityMember;
import Projeto_BRASFI.api_brasfi_backend.domain.member.community.CommunityMemberService;
import Projeto_BRASFI.api_brasfi_backend.domain.member.community.RoleRequest;
import Projeto_BRASFI.api_brasfi_backend.domain.member.follow.MemberFollowService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/member")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private final MemberService service;
    private final MemberBlockService memberBlockService;
    private final MemberFollowService memberFollowService;
    private final CommunityMemberService communityMemberService;

    public MemberController(MemberService service, MemberBlockService memberBlockService,
                            MemberFollowService memberFollowService, CommunityMemberService communityMemberService) {
        this.service = service;
        this.memberBlockService = memberBlockService;
        this.memberFollowService = memberFollowService;
        this.communityMemberService = communityMemberService;
    }

    @PostMapping
    public ResponseEntity<Member> create(@RequestBody @Valid MemberDto dto) {
        logger.info("Recebida requisição POST para /member");
        logger.debug("Dados recebidos: {}", dto);
        try {
            Member member = service.create(dto);
            logger.info("Membro criado com sucesso. ID: {}", member.getId());
            return ResponseEntity.created(URI.create("/member/" + member.getId())).body(member);
        } catch (Exception e) {
            logger.error("Erro ao criar membro: {}", e.getMessage(), e);
            throw e;
        }
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

    @PostMapping("/{memberId}/join-community/{communityId}")
    public ResponseEntity<Void> joinCommunity(
            @PathVariable Long memberId,
            @PathVariable Long communityId,
            @RequestBody(required = false) RoleRequest roleRequest) {

        CommunityMember.Role role = CommunityMember.Role.MEMBER;

        if (roleRequest != null && roleRequest.getRole() != null) {
            role = roleRequest.getRole();
        }

        communityMemberService.addMemberToCommunity(communityId, memberId, role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{memberId}/leave-community/{communityId}")
    public ResponseEntity<Void> leaveCommunity(@PathVariable Long memberId, @PathVariable Long communityId) {
        communityMemberService.removeMemberFromCommunity(communityId, memberId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{memberId}/change-role/{communityId}")
    public ResponseEntity<Void> changeCommunityRole(
            @PathVariable Long memberId,
            @PathVariable Long communityId,
            @RequestBody(required = false) RoleRequest roleRequest) {

        CommunityMember.Role role = CommunityMember.Role.MEMBER;

        if (roleRequest != null && roleRequest.getRole() != null) {
            role = roleRequest.getRole();
        }

        communityMemberService.changeRole(communityId, memberId, role);
        return ResponseEntity.ok().build();
    }

}
