package Projeto_BRASFI.api_brasfi_backend.domain.member;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member authenticate(String username, String password) {
        Optional<Member> memberOptional = repository.findByUsername(username);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            if (passwordEncoder.matches(password, member.getPasswordHash())) {
                return member;
            }
        }
        throw new RuntimeException("Credenciais inválidas ou usuário não encontrado.");
    }

    public Member create(MemberDto dto) {
        if (repository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Usuário com este nome de usuário já existe.");
        }
        if (repository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Usuário com este email já existe.");
        }

        String hashedPassword = passwordEncoder.encode(dto.passwordHash());

        Member member = new Member(
                null,
                dto.name(),
                dto.username(),
                dto.email(),
                hashedPassword,
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
                .orElseThrow(() -> new EntityNotFoundException("Membro não encontrado com ID: " + id));
    }

    public Member update(Long id, MemberUpdateDto dto) {
        Member member = findById(id);

        if (StringUtils.hasText(dto.name()) && !dto.name().equals(member.getName())) {
            member.setName(dto.name());
        }

        if (StringUtils.hasText(dto.email()) && !dto.email().equals(member.getEmail())) {
            if (repository.existsByEmailAndIdNot(dto.email(), id)) {
                throw new IllegalArgumentException("Usuário com este email já existe.");
            }
            member.setEmail(dto.email());
        }

        if (dto.description() != null && !dto.description().equals(member.getDescription())) {
            member.setDescription(dto.description());
        }

        if (StringUtils.hasText(dto.newPassword())) {
            member.setPasswordHash(passwordEncoder.encode(dto.newPassword()));
        }

        return repository.save(member);
    }


    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Membro não encontrado com ID: " + id + " para exclusão.");
        }
        repository.deleteById(id);
    }
}

