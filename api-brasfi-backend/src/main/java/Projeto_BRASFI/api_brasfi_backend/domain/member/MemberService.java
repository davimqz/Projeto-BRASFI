package Projeto_BRASFI.api_brasfi_backend.domain.member;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public Member authenticate(String username, String password) {
        Optional<Member> member = repository.findByUsername(username);
        if (member.isPresent() && member.get().getPasswordHash().equals(password)) {
            return member.get();
        }
        throw new RuntimeException("Credenciais inválidas");
    }

    public Member create(MemberDto dto) {
        if (repository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("User with this username already exists.");
        }
        if (repository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        Member member = new Member(
                null,
                dto.name(),
                dto.username(),
                dto.email(),
                dto.passwordHash(),
                LocalDateTime.now(),
                dto.description()
        );

        return repository.save(member);
    }


    public List<Member> findAll() {
        return repository.findAll();
    }

    public Member findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
    }

    public Member update(Long id, MemberDto dto) {
        if (repository.existsByUsernameAndIdNot(dto.username(), id)) {
            throw new IllegalArgumentException("User with this username already exists.");
        }
        if (repository.existsByEmailAndIdNot(dto.email(), id)) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        Member member = findById(id);
        Member updated = new Member(
                id,
                dto.name(),
                dto.username(),
                dto.email(),
                dto.passwordHash(),
                member.getCreatedAt(),
                dto.description()
        );
        return repository.save(updated);
    }


    public void delete(Long id) {
        repository.deleteById(id);
    }
}

