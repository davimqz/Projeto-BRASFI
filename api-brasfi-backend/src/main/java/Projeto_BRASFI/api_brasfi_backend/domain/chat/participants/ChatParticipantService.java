package Projeto_BRASFI.api_brasfi_backend.domain.chat.participants;

import Projeto_BRASFI.api_brasfi_backend.domain.chat.Chat;
import Projeto_BRASFI.api_brasfi_backend.domain.chat.ChatRepository;
import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import Projeto_BRASFI.api_brasfi_backend.domain.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatParticipantService {

    private final ChatParticipantRepository repository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ChatParticipant addParticipant(Long chatId, Long memberId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        ChatParticipantId id = new ChatParticipantId(chatId, memberId);
        if (repository.existsById(id)) {
            throw new IllegalStateException("Member already in chat");
        }
        ChatParticipant cp = new ChatParticipant();
        cp.setId(id);
        cp.setChat(chat);
        cp.setMember(member);
        return repository.save(cp);
    }


    @Transactional
    public void removeParticipant(Long chatId, Long memberId) {
        ChatParticipantId id = new ChatParticipantId(chatId, memberId);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Chat participant not found");
        }
        repository.deleteById(id);
    }

    public List<ChatParticipant> getParticipantsByChatId(Long chatId) {
        return repository.findByChatId(chatId);
    }
}
