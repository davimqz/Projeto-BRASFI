package Projeto_BRASFI.api_brasfi_backend.domain.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
// As anotações @NotBlank podem ser removidas se os campos são verdadeiramente opcionais na atualização.
// Se alguns ainda são obrigatórios (como e-mail, nome), mantenha @NotBlank.
// Para este exemplo, vamos assumir que name e email são obrigatórios mesmo na atualização.

public record MemberUpdateDto(
    @NotBlank(message = "O nome não pode estar em branco.") 
    String name,

    @NotBlank(message = "O email não pode estar em branco.")
    @Email(message = "Formato de email inválido.") 
    String email,
    
    String description,  // Opcional, pode ser nulo ou string vazia
    String newPassword   // Opcional, senha em texto plano
) {} 