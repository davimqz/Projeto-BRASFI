package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.message.Message;
import Projeto_BRASFI.api_brasfi_backend.domain.message.MessageDto;
import Projeto_BRASFI.api_brasfi_backend.domain.message.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public Message sendMessage(@RequestBody MessageDto dto) {
        return messageService.sendMessage(dto);
    }

    @GetMapping("/inbox/{recipientId}")
    public List<Message> getInbox(@PathVariable Long recipientId) {
        return messageService.getInbox(recipientId);
    }

    @GetMapping("/conversation")
    public List<Message> getConversation(@RequestParam Long senderId, @RequestParam Long recipientId) {
        return messageService.getConversation(senderId, recipientId);
    }
}
