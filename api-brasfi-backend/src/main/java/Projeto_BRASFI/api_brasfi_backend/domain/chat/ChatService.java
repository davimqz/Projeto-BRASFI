package Projeto_BRASFI.api_brasfi_backend.domain.chat;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat create(ChatDto dto) {
        Chat chat = new Chat(null, dto.type(), LocalDateTime.now());
        return chatRepository.save(chat);
    }

    public Optional<Chat> findById(Long id) {
        return chatRepository.findById(id);
    }

    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public Chat update(Long id, ChatDto dto) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        chat.setType(dto.type());
        return chatRepository.save(chat);
    }

    public void delete(Long id) {
        chatRepository.deleteById(id);
    }
}
