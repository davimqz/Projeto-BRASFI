package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service // Para que possa ser injetado
public class JwtTokenServiceImpl implements TokenService {

    // !!! IMPORTANTE: Mova para configuração externa em produção !!!
    private static final String JWT_SECRET = "your-very-secure-secret-key-here-please-change-me"; 
    private static final String ISSUER = "api-brasfi-backend";
    private static final long EXPIRATION_TIME_HOURS = 2; // Token expira em 2 horas

    @Override
    public String generateToken(Member member) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(member.getUsername()) // Identifica o usuário pelo username
                    .withClaim("id", member.getId())       // Adiciona o ID do usuário como uma claim
                    .withClaim("name", member.getName())   // Adiciona o nome para conveniência (opcional)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);
        } catch (Exception exception){
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    private Instant getExpirationDate(){
        return LocalDateTime.now().plusHours(EXPIRATION_TIME_HOURS).toInstant(ZoneOffset.of("-03:00")); // Ajuste o ZoneOffset conforme necessário
    }
} 