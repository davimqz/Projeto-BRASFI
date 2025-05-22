package Projeto_BRASFI.api_brasfi_backend.domain.community.invitation;

public class InvitationRequest {
    private Integer communityId;
    private Integer invitedMemberId;
    private Integer invitedBy;

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public Integer getInvitedMemberId() {
        return invitedMemberId;
    }

    public void setInvitedMemberId(Integer invitedMemberId) {
        this.invitedMemberId = invitedMemberId;
    }

    public Integer getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(Integer invitedBy) {
        this.invitedBy = invitedBy;
    }
}
