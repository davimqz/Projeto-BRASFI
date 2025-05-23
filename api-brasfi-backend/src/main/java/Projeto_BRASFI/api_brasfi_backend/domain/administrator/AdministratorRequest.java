package Projeto_BRASFI.api_brasfi_backend.domain.administrator;


import jakarta.validation.constraints.NotNull;

public record AdministratorRequest(@NotNull Integer memberId) {
}
