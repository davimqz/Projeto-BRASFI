package Projeto_BRASFI.api_brasfi_backend.domain.member.follow;

import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import Projeto_BRASFI.api_brasfi_backend.domain.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberFollowService {

    private final MemberFollowRepository memberFollowRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void followMember(Long memberId, Long followerId) {
        if (memberId.equals(followerId)) {
            throw new RuntimeException("You cannot follow yourself.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member to be followed not found."));

        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found."));

        MemberFollowId id = new MemberFollowId(member.getId(), follower.getId());

        if (memberFollowRepository.existsById(id)) {
            throw new RuntimeException("You are already following this member.");
        }

        MemberFollow memberFollow = new MemberFollow();
        memberFollow.setId(id);
        memberFollow.setMember(member);
        memberFollow.setFollower(follower);

        memberFollowRepository.save(memberFollow);
    }

    @Transactional
    public void unfollowMember(Long memberId, Long followerId) {
        MemberFollowId id = new MemberFollowId(memberId, followerId);

        if (!memberFollowRepository.existsById(id)) {
            throw new RuntimeException("You are not following this member.");
        }

        memberFollowRepository.deleteById(id);
    }
}
