package Projeto_BRASFI.api_brasfi_backend.domain.member;

import Projeto_BRASFI.api_brasfi_backend.domain.education.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);


}
