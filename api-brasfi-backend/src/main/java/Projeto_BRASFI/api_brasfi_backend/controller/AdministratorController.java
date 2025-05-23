package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.administrator.Administrator;
import Projeto_BRASFI.api_brasfi_backend.domain.administrator.AdministratorRequest;
import Projeto_BRASFI.api_brasfi_backend.domain.administrator.AdministratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {

    private final AdministratorService service;

    public AdministratorController(AdministratorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Administrator> create(@RequestBody AdministratorRequest request) {
        try {
            Administrator admin = service.create(request.memberId());
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrator> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-member/{memberId}")
    public ResponseEntity<Administrator> getByMemberId(@PathVariable Integer memberId) {
        return service.getByMemberId(memberId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
