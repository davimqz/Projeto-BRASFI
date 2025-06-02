package Projeto_BRASFI.api_brasfi_backend.domain.message;

public record MessageDto(Long senderId, Long recipientId, String content) {
}
