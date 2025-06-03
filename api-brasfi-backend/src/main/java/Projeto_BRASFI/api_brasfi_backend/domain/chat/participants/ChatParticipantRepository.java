package Projeto_BRASFI.api_brasfi_backend.domain.chat.participants;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, ChatParticipantId> {
    List<ChatParticipant> findByChatId(Long chatId);
}
