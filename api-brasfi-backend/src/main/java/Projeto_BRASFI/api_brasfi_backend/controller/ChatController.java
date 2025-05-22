package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.chat.Chat;
import Projeto_BRASFI.api_brasfi_backend.domain.chat.ChatDto;
import Projeto_BRASFI.api_brasfi_backend.domain.chat.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<Chat> create(@RequestBody ChatDto dto) {
        return ResponseEntity.ok(chatService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chat> get(@PathVariable Long id) {
        return chatService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Chat>> list() {
        return ResponseEntity.ok(chatService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chat> update(@PathVariable Long id, @RequestBody ChatDto dto) {
        return ResponseEntity.ok(chatService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
