package Projeto_BRASFI.api_brasfi_backend.domain.member.follow;

import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_followers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class MemberFollow {

    @EmbeddedId
    private MemberFollowId id;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
    private Member follower;
}
