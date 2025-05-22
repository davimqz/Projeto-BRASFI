package Projeto_BRASFI.api_brasfi_backend.domain.member.community;

import Projeto_BRASFI.api_brasfi_backend.domain.community.Community;
import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Community_Members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityMember {

    @EmbeddedId
    private CommunityMemberId id;

    @ManyToOne
    @MapsId("communityId")
    private Community community;

    @ManyToOne
    @MapsId("memberId")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime joinedAt;

    public enum Role {
        MEMBER,
        LEADER
    }

}
