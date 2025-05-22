package Projeto_BRASFI.api_brasfi_backend.domain.member;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberEducationRepository extends JpaRepository<MemberEducation, Long> {

    List<MemberEducation> findByMemberId(Long memberId);

    void deleteByMemberIdAndEducationId(Long memberId, Long educationId);

    @Modifying
    @Transactional
    @Query("delete from MemberEducation me where me.education.id = :educationId")
    void deleteByEducationId(@Param("educationId") Long educationId);
}