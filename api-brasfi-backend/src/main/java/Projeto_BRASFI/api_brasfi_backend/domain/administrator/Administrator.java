package Projeto_BRASFI.api_brasfi_backend.domain.administrator;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "administrator")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Administrator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id", unique = true)
    private Integer memberId;
}
