package Projeto_BRASFI.api_brasfi_backend.domain.message;

import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import Projeto_BRASFI.api_brasfi_backend.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepo;
    private final MemberRepository memberRepo;

    public MessageService(MessageRepository messageRepo, MemberRepository memberRepo) {
        this.messageRepo = messageRepo;
        this.memberRepo = memberRepo;
    }

    @Transactional
    public Message sendMessage(MessageDto dto) {
        Member sender = memberRepo.findById(dto.senderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        Member recipient = memberRepo.findById(dto.recipientId())
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        Message message = new Message(sender, recipient, dto.content());
        return messageRepo.save(message);
    }

    public List<Message> getInbox(Long recipientId) {
        Member recipient = memberRepo.findById(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));
        return messageRepo.findByRecipient(recipient);
    }

    public List<Message> getConversation(Long senderId, Long recipientId) {
        Member sender = memberRepo.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        Member recipient = memberRepo.findById(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        return messageRepo.findBySenderAndRecipient(sender, recipient);
    }
}
