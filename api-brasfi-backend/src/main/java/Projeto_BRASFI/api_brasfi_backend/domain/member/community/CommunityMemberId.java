package Projeto_BRASFI.api_brasfi_backend.domain.member.community;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CommunityMemberId implements Serializable {

    private Long communityId;
    private Long memberId;

    public CommunityMemberId() {}

    public CommunityMemberId(Long communityId, Long memberId) {
        this.communityId = communityId;
        this.memberId = memberId;
    }

    // getters e setters
    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    // equals e hashCode obrigatórios para ID composto
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommunityMemberId)) return false;
        CommunityMemberId that = (CommunityMemberId) o;
        return Objects.equals(getCommunityId(), that.getCommunityId()) &&
                Objects.equals(getMemberId(), that.getMemberId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommunityId(), getMemberId());
    }
}
