package Projeto_BRASFI.api_brasfi_backend.domain.education;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "education")
@Entity(name = "Education")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String institution;

    @Enumerated(EnumType.STRING)
    private EducationType type;

    private Integer startYear;
    private Integer endYear;
}
