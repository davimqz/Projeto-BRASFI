package Projeto_BRASFI.api_brasfi_backend.domain.member.education;

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
