package Projeto_BRASFI.api_brasfi_backend.domain.invitation;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

    boolean existsByCommunityIdAndInvitedMemberId(Integer communityId, Integer invitedMemberId);

}
