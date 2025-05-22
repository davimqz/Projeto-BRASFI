package Projeto_BRASFI.api_brasfi_backend.domain.community.invitation;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "community_id", nullable = false)
    private Integer communityId;

    @Column(name = "invited_member_id")
    private Integer invitedMemberId;

    @Column(name = "invited_by")
    private Integer invitedBy;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "accepted")
    private Boolean accepted;
}
