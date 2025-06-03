package Projeto_BRASFI.api_brasfi_backend.domain.education;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EducationDto {
    @NotBlank
    private String title;
    @NotBlank
    private String institution;
    @NotBlank
    private EducationType type;
    private Integer startYear;
    private Integer endYear;
}