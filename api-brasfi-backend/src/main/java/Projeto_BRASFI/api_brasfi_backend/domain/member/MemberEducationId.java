package Projeto_BRASFI.api_brasfi_backend.domain.member;

import Projeto_BRASFI.api_brasfi_backend.domain.education.Education;
import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberEducationId implements Serializable {
    private Long member;
    private Long education;
}
