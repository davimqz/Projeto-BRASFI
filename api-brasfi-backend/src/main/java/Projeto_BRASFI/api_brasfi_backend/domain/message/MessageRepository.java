package Projeto_BRASFI.api_brasfi_backend.domain.message;

import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndRecipient(Member sender, Member recipient);
    List<Message> findByRecipient(Member recipient);
}
