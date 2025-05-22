package Projeto_BRASFI.api_brasfi_backend.domain.member.block;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBlockRepository extends JpaRepository<MemberBlock, MemberBlockId> {

    void deleteById(MemberBlockId id);

    boolean existsById(MemberBlockId id);
}
