package Projeto_BRASFI.api_brasfi_backend.controller;

// Adicione getters se não estiver usando records, ou use Lombok @Data se preferir classes tradicionais.
// Usando record para simplicidade e imutabilidade.
public record LoginResponseDto(
    Long id,
    String name,
    String username,
    String email,
    String description,
    String token
) {} 