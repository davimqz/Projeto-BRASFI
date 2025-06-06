// AuthController.java

package Projeto_BRASFI.api_brasfi_backend.controller;

import Projeto_BRASFI.api_brasfi_backend.domain.member.Member;
import Projeto_BRASFI.api_brasfi_backend.domain.member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private final MemberService memberService;
    private final TokenService tokenService;

    public AuthController(MemberService memberService, TokenService tokenService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            Member member = memberService.authenticate(username, password);
            
            String jwtToken = tokenService.generateToken(member);

            LoginResponseDto loginResponse = new LoginResponseDto(
                member.getId(),
                member.getName(),
                member.getUsername(),
                member.getEmail(),
                member.getDescription(),
                jwtToken
            );

            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Credenciais inválidas ou erro no login: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 