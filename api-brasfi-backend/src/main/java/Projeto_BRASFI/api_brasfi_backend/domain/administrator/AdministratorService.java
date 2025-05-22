package Projeto_BRASFI.api_brasfi_backend.domain.administrator;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdministratorService {

    private final AdministratorRepository repository;

    public AdministratorService(AdministratorRepository repository) {
        this.repository = repository;
    }

    public Administrator create(Integer memberId) {
        if (repository.existsByMemberId(memberId)) {
            throw new RuntimeException("Administrator with this memberId already exists");
        }
        Administrator admin = Administrator.builder().memberId(memberId).build();
        return repository.save(admin);
    }

    public Optional<Administrator> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Administrator> getByMemberId(Integer memberId) {
        return repository.findByMemberId(memberId);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
