package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.invitation.Invitation;
import Projeto_BRASFI.api_brasfi_backend.domain.invitation.InvitationRequest;
import Projeto_BRASFI.api_brasfi_backend.domain.invitation.InvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

    private final InvitationService service;

    public InvitationController(InvitationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Invitation> createInvitation(@RequestBody InvitationRequest request) {
        Invitation invitation = service.createInvitation(
                request.getCommunityId(),
                request.getInvitedMemberId(),
                request.getInvitedBy()
        );
        return ResponseEntity.ok(invitation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invitation> getInvitation(@PathVariable Integer id) {
        return service.getInvitation(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<Invitation> acceptInvitation(@PathVariable Integer id) {
        try {
            Invitation updated = service.acceptInvitation(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvitation(@PathVariable Integer id) {
        service.deleteInvitation(id);
        return ResponseEntity.noContent().build();
    }
}
