package Projeto_BRASFI.api_brasfi_backend.domain.member.education;

import Projeto_BRASFI.api_brasfi_backend.domain.education.Education;
import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "member_education")
@Entity
@IdClass(MemberEducationId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"member", "education"})
public class MemberEducation {

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "education_id")
    private Education education;
}