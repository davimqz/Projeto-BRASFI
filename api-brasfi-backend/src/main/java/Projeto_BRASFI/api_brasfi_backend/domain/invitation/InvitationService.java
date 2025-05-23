package Projeto_BRASFI.api_brasfi_backend.domain.invitation;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InvitationService {

    private final InvitationRepository repository;

    public InvitationService(InvitationRepository repository) {
        this.repository = repository;
    }

    public Invitation createInvitation(Integer communityId, Integer invitedMemberId, Integer invitedBy) {
        if (repository.existsByCommunityIdAndInvitedMemberId(communityId, invitedMemberId)) {
            throw new RuntimeException("Invitation already exists for this member in the community");
        }

        Invitation invitation = Invitation.builder()
                .communityId(communityId)
                .invitedMemberId(invitedMemberId)
                .invitedBy(invitedBy)
                .sentAt(LocalDateTime.now())
                .accepted(false)
                .build();

        return repository.save(invitation);
    }

    public Optional<Invitation> getInvitation(Integer id) {
        return repository.findById(id);
    }

    public Invitation acceptInvitation(Integer id) {
        Optional<Invitation> optional = repository.findById(id);
        if (optional.isPresent()) {
            Invitation invitation = optional.get();
            invitation.setAccepted(true);
            return repository.save(invitation);
        }
        throw new RuntimeException("Invitation not found");
    }

    public void deleteInvitation(Integer id) {
        repository.deleteById(id);
    }
}
