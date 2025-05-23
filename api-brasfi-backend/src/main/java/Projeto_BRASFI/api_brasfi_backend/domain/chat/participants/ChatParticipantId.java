package Projeto_BRASFI.api_brasfi_backend.domain.chat.participants;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;

@Embeddable
public class ChatParticipantId implements Serializable {

    private Long chatId;
    private Long memberId;

    public ChatParticipantId() {}

    public ChatParticipantId(Long chatId, Long memberId) {
        this.chatId = chatId;
        this.memberId = memberId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatParticipantId)) return false;
        ChatParticipantId that = (ChatParticipantId) o;
        return Objects.equals(chatId, that.chatId) &&
                Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, memberId);
    }
}
