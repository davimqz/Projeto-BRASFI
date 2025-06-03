package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.community.Community;
import Projeto_BRASFI.api_brasfi_backend.domain.community.CommunityDto;
import Projeto_BRASFI.api_brasfi_backend.domain.community.CommunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping
    public ResponseEntity<Community> create(@RequestBody CommunityDto dto) {
        return ResponseEntity.ok(communityService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Community> get(@PathVariable Long id) {
        return ResponseEntity.ok(communityService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Community>> list() {
        return ResponseEntity.ok(communityService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Community> update(@PathVariable Long id, @RequestBody CommunityDto dto) {
        return ResponseEntity.ok(communityService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        communityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
