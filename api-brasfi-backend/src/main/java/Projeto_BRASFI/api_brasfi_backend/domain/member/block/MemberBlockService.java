package Projeto_BRASFI.api_brasfi_backend.domain.member.block;

import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import Projeto_BRASFI.api_brasfi_backend.domain.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberBlockService {

    private final MemberBlockRepository memberBlockRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void blockMember(Long blockerId, Long blockedId) {
        if (blockerId.equals(blockedId)) {
            throw new RuntimeException("You cannot block yourself.");
        }

        Member blocker = memberRepository.findById(blockerId)
                .orElseThrow(() -> new RuntimeException("Blocker not found."));

        Member blocked = memberRepository.findById(blockedId)
                .orElseThrow(() -> new RuntimeException("Blocked member not found."));

        MemberBlockId id = new MemberBlockId(blocker.getId(), blocked.getId());

        if (memberBlockRepository.existsById(id)) {
            throw new RuntimeException("You have already blocked this member.");
        }

        MemberBlock memberBlock = new MemberBlock();
        memberBlock.setId(id);
        memberBlock.setBlocker(blocker);
        memberBlock.setBlocked(blocked);

        memberBlockRepository.save(memberBlock);
    }

    @Transactional
    public void unblockMember(Long blockerId, Long blockedId) {
        MemberBlockId id = new MemberBlockId(blockerId, blockedId);

        if (!memberBlockRepository.existsById(id)) {
            throw new RuntimeException("You have not blocked this member.");
        }

        memberBlockRepository.deleteById(id);
    }
}
