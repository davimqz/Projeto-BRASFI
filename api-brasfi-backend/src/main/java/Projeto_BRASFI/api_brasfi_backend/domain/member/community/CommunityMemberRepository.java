package Projeto_BRASFI.api_brasfi_backend.domain.member.community;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, CommunityMemberId> {
}
