package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;

public interface TokenService {
    String generateToken(Member member);
} 