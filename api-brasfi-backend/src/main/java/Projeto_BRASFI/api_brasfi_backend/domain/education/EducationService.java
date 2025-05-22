package Projeto_BRASFI.api_brasfi_backend.domain.education;

import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import Projeto_BRASFI.api_brasfi_backend.domain.member.MemberEducation;
import Projeto_BRASFI.api_brasfi_backend.domain.member.MemberEducationRepository;
import Projeto_BRASFI.api_brasfi_backend.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationService {

    private final EducationRepository educationRepository;
    private final MemberRepository memberRepository;
    private final MemberEducationRepository memberEducationRepository;

    public EducationService(EducationRepository educationRepository,
                            MemberRepository memberRepository,
                            MemberEducationRepository memberEducationRepository) {
        this.educationRepository = educationRepository;
        this.memberRepository = memberRepository;
        this.memberEducationRepository = memberEducationRepository;
    }

    public Education create(EducationDto dto) {
        Education education = new Education();
        education.setTitle(dto.getTitle());
        education.setInstitution(dto.getInstitution());
        education.setType(dto.getType());
        education.setStartYear(dto.getStartYear());
        education.setEndYear(dto.getEndYear());
        return educationRepository.save(education);
    }

    public Education update(Long id, EducationDto dto) {
        Education education = educationRepository.findById(id).orElseThrow();
        education.setTitle(dto.getTitle());
        education.setInstitution(dto.getInstitution());
        education.setType(dto.getType());
        education.setStartYear(dto.getStartYear());
        education.setEndYear(dto.getEndYear());
        return educationRepository.save(education);
    }

    @Transactional
    public void delete(Long educationId) {
        memberEducationRepository.deleteByEducationId(educationId);
        educationRepository.deleteById(educationId);
    }

    public List<Education> findAll() {
        return educationRepository.findAll();
    }

    public List<Education> getByMember(Long memberId) {
        List<MemberEducation> associations = memberEducationRepository.findByMemberId(memberId);
        return associations.stream()
                .map(MemberEducation::getEducation)
                .toList();
    }

    public void assignToMember(Long memberId, Long educationId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Education education = educationRepository.findById(educationId).orElseThrow();

        MemberEducation memberEducation = new MemberEducation();
        memberEducation.setMember(member);
        memberEducation.setEducation(education);

        memberEducationRepository.save(memberEducation);
    }

    public void removeFromMember(Long memberId, Long educationId) {

        memberEducationRepository.deleteByMemberIdAndEducationId(memberId, educationId);
    }
}
