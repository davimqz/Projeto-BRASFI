// MemberController.java

package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.member.*;
import Projeto_BRASFI.api_brasfi_backend.domain.member.block.MemberBlockService;
import Projeto_BRASFI.api_brasfi_backend.domain.member.community.CommunityMember;
import Projeto_BRASFI.api_brasfi_backend.domain.member.community.CommunityMemberService;
import Projeto_BRASFI.api_brasfi_backend.domain.member.community.RoleRequest;
import Projeto_BRASFI.api_brasfi_backend.domain.member.follow.MemberFollowService;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class ErrorResponse {
        private String error;
        private String message;
        private String path;
        private Integer status;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public ErrorResponse(String error, String message, Integer status) {
            this.error = error;
            this.message = message;
            this.status = status;
        }
        
        public String getError() { return error; }
        public String getMessage() { return message; }
        public String getPath() { return path; }
        public Integer getStatus() { return status; }
    }

    public MemberController(MemberService service, MemberBlockService memberBlockService,
                            MemberFollowService memberFollowService, CommunityMemberService communityMemberService) {
        this.service = service;
        this.memberBlockService = memberBlockService;
        this.memberFollowService = memberFollowService;
        this.communityMemberService = communityMemberService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid MemberDto dto) {
        logger.info("Recebida requisição POST para /member para criar usuário");
        logger.debug("Dados recebidos para criação: {}", dto);
        try {
            Member member = service.create(dto);
            logger.info("Membro criado com sucesso. ID: {}", member.getId());
            return ResponseEntity.created(URI.create("/member/" + member.getId())).body(member);
        } catch (IllegalArgumentException e) {
            logger.error("Erro de argumento ao criar membro: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage(), 400));
        } catch (Exception e) {
            logger.error("Erro genérico ao criar membro: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Server Error", "Erro ao criar membro.", 500));
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
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid MemberUpdateDto dto) {
        logger.info("Controller: Recebida requisição PUT para /member/{}", id);
        logger.info("Controller: DTO recebido: {}", dto);
        try {
            Member updatedMember = service.update(id, dto);
            logger.info("Controller: Membro {} atualizado com sucesso.", id);
            return ResponseEntity.ok(updatedMember);
        } catch (IllegalArgumentException e) {
            logger.error("Controller: Erro de argumento inválido ao atualizar membro {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage(), 400));
        } catch (jakarta.persistence.EntityNotFoundException e) {
            logger.error("Controller: Membro não encontrado para atualização {}: {}", id, e.getMessage());
            return ResponseEntity.status(404).body(new ErrorResponse("Not Found", e.getMessage(), 404));
        }
        catch (Exception e) {
            logger.error("Controller: Erro genérico ao atualizar membro {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Server Error", "Erro ao atualizar perfil.", 500));
        }
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
