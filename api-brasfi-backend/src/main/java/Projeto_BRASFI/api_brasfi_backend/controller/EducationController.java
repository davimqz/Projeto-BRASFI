package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.education.Education;
import Projeto_BRASFI.api_brasfi_backend.domain.education.EducationDto;
import Projeto_BRASFI.api_brasfi_backend.domain.education.EducationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/education")
public class EducationController {

    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @PostMapping
    public ResponseEntity<Education> create(@RequestBody EducationDto dto) {
        return ResponseEntity.ok(educationService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Education> update(@PathVariable Long id, @RequestBody EducationDto dto) {
        return ResponseEntity.ok(educationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        educationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Education>> getAll() {
        return ResponseEntity.ok(educationService.findAll());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Education>> getByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(educationService.getByMember(memberId));
    }

    @PostMapping("/assign/{memberId}/{educationId}")
    public ResponseEntity<Void> assign(@PathVariable Long memberId, @PathVariable Long educationId) {
        educationService.assignToMember(memberId, educationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{memberId}/{educationId}")
    public ResponseEntity<Void> remove(@PathVariable Long memberId, @PathVariable Long educationId) {
        educationService.removeFromMember(memberId, educationId);
        return ResponseEntity.ok().build();
    }
}
