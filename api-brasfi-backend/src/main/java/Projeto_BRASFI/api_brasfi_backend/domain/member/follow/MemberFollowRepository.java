package Projeto_BRASFI.api_brasfi_backend.domain.member.follow;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberFollowRepository extends JpaRepository<MemberFollow, MemberFollowId> {

    void deleteById(MemberFollowId id);

    boolean existsById(MemberFollowId id);
}
