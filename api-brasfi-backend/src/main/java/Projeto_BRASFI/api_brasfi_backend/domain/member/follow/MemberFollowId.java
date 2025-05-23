package Projeto_BRASFI.api_brasfi_backend.domain.member.follow;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberFollowId implements Serializable {

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "follower_id")
    private Long followerId;
}
