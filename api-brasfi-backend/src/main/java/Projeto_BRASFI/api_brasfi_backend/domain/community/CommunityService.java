package Projeto_BRASFI.api_brasfi_backend.domain.community;

import Projeto_BRASFI.api_brasfi_backend.domain.chat.Chat;
import Projeto_BRASFI.api_brasfi_backend.domain.chat.ChatRepository;
import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import Projeto_BRASFI.api_brasfi_backend.domain.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    public CommunityService(CommunityRepository communityRepository, ChatRepository chatRepository, MemberRepository memberRepository) {
        this.communityRepository = communityRepository;
        this.chatRepository = chatRepository;
        this.memberRepository = memberRepository;
    }

    public Community create(CommunityDto dto) {
        Chat chat = chatRepository.findById(dto.chatId())
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        Member creator = memberRepository.findById(dto.creatorId())
                .orElseThrow(() -> new IllegalArgumentException("Creator member not found"));

        Community community = new Community(null, dto.name(), dto.description(), chat, creator);
        return communityRepository.save(community);
    }

    public Community findById(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));
    }

    public List<Community> findAll() {
        return communityRepository.findAll();
    }

    public Community update(Long id, CommunityDto dto) {
        Community community = findById(id);
        Chat chat = chatRepository.findById(dto.chatId())
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        Member creator = memberRepository.findById(dto.creatorId())
                .orElseThrow(() -> new IllegalArgumentException("Creator member not found"));

        community.setName(dto.name());
        community.setDescription(dto.description());
        community.setChat(chat);
        community.setCreator(creator);

        return communityRepository.save(community);
    }

    public void delete(Long id) {
        communityRepository.deleteById(id);
    }
}
