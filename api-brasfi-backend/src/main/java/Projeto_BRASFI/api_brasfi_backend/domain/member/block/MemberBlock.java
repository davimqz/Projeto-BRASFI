package Projeto_BRASFI.api_brasfi_backend.domain.member.block;

import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_blocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class MemberBlock {

    @EmbeddedId
    private MemberBlockId id;

    @ManyToOne
    @MapsId("blockerId")
    @JoinColumn(name = "blocker_id")
    private Member blocker;

    @ManyToOne
    @MapsId("blockedId")
    @JoinColumn(name = "blocked_id")
    private Member blocked;
}
