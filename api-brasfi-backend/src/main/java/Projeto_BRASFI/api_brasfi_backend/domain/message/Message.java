package Projeto_BRASFI.api_brasfi_backend.domain.message;

import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Member sender;

    @ManyToOne(optional = false)
    private Member recipient;

    private String content;
    private LocalDateTime sentAt;

    public Message() {
        this.sentAt = LocalDateTime.now();
    }

    public Message(Member sender, Member recipient, String content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.sentAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Member getSender() { return sender; }
    public Member getRecipient() { return recipient; }
    public String getContent() { return content; }
    public LocalDateTime getSentAt() { return sentAt; }

    public void setId(Long id) { this.id = id; }
    public void setSender(Member sender) { this.sender = sender; }
    public void setRecipient(Member recipient) { this.recipient = recipient; }
    public void setContent(String content) { this.content = content; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}
