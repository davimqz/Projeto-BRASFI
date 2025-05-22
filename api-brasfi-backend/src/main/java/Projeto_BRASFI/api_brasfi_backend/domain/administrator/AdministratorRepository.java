package Projeto_BRASFI.api_brasfi_backend.domain.administrator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    Optional<Administrator> findByMemberId(Integer memberId);
    boolean existsByMemberId(Integer memberId);
}
