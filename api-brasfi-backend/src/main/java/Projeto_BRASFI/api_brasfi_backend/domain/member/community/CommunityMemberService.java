package Projeto_BRASFI.api_brasfi_backend.domain.member.community;

import Projeto_BRASFI.api_brasfi_backend.domain.community.Community;
import Projeto_BRASFI.api_brasfi_backend.domain.community.CommunityRepository;
import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import Projeto_BRASFI.api_brasfi_backend.domain.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommunityMemberService {

    private final CommunityMemberRepository repository;
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void addMemberToCommunity(Long communityId, Long memberId, CommunityMember.Role role) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        CommunityMemberId id = new CommunityMemberId(communityId, memberId);

        if (repository.existsById(id)) {
            throw new IllegalStateException("Member is already in the community");
        }

        CommunityMember communityMember = new CommunityMember();
        communityMember.setId(id);
        communityMember.setCommunity(community);
        communityMember.setMember(member);
        communityMember.setRole(role);
        communityMember.setJoinedAt(LocalDateTime.now());

        repository.save(communityMember);
    }

    @Transactional
    public void removeMemberFromCommunity(Long communityId, Long memberId) {
        CommunityMemberId id = new CommunityMemberId(communityId, memberId);

        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Membership not found");
        }

        repository.deleteById(id);
    }

    @Transactional
    public void changeRole(Long communityId, Long memberId, CommunityMember.Role newRole) {
        CommunityMemberId id = new CommunityMemberId(communityId, memberId);

        CommunityMember communityMember = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Membership not found"));

        communityMember.setRole(newRole);
    }
}
