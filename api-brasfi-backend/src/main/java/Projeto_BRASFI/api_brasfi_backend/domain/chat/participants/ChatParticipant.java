package Projeto_BRASFI.api_brasfi_backend.domain.chat.participants;

import Projeto_BRASFI.api_brasfi_backend.domain.chat.Chat;
import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ChatParticipant {

    @EmbeddedId
    private ChatParticipantId id;

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;
}
