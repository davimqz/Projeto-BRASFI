package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.chat.Chat;
import Projeto_BRASFI.api_brasfi_backend.domain.chat.ChatDto;
import Projeto_BRASFI.api_brasfi_backend.domain.chat.ChatService;
import Projeto_BRASFI.api_brasfi_backend.domain.chat.participants.ChatParticipant;
import Projeto_BRASFI.api_brasfi_backend.domain.chat.participants.ChatParticipantDto;
import Projeto_BRASFI.api_brasfi_backend.domain.chat.participants.ChatParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final ChatParticipantService participantService;

    public ChatController(ChatService chatService, ChatParticipantService participantService) {
        this.chatService = chatService;
        this.participantService = participantService;
    }

    // Chat endpoints

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

    @PostMapping("/{chatId}/participant/{memberId}")
    public ResponseEntity<ChatParticipant> addParticipant(@PathVariable Long chatId, @PathVariable Long memberId) {
        ChatParticipant participant = participantService.addParticipant(chatId, memberId);
        return ResponseEntity.ok(participant);
    }

    @DeleteMapping("/{chatId}/participant/{memberId}")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long chatId, @PathVariable Long memberId) {
        participantService.removeParticipant(chatId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{chatId}/participant")
    public ResponseEntity<List<ChatParticipant>> listParticipants(@PathVariable Long chatId) {
        return ResponseEntity.ok(participantService.getParticipantsByChatId(chatId));
    }
}
