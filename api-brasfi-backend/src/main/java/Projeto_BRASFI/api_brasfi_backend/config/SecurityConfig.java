// SecurityConfig.java
package Projeto_BRASFI.api_brasfi_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Import para .csrf(AbstractHttpConfigurer::disable)
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults; // Pode ser necessário para .cors(withDefaults()) se usar

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsFilter corsFilter;

    public SecurityConfig(CorsFilter corsFilter) {
        this.corsFilter = corsFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(corsFilter, ChannelProcessingFilter.class) // Seu filtro CORS customizado
            // Se seu corsFilter já é um bean gerenciado pelo Spring (ex: de CorsConfig),
            // você pode não precisar de .cors(cors -> cors.disable()) se o seu filtro já faz tudo.
            // Ou, se quiser usar o mecanismo do Spring Security com base no seu CorsFilter (se ele for um CorsConfigurationSource):
            // .cors(withDefaults()) // ou .cors(cors -> cors.configurationSource(meuCorsConfigurationSourceBean))
            // Por enquanto, manter desabilitado para confiar no seu filtro:
            .cors(AbstractHttpConfigurer::disable) // Ou mantenha cors -> cors.disable() se AbstractHttpConfigurer não for o que você quer
            .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF
            .authorizeHttpRequests(auth -> {
                auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Permite todas as requisições OPTIONS
                    .requestMatchers("/actuator/**").permitAll()          // Permite TODOS os endpoints do Actuator
                    .requestMatchers("/auth/**").permitAll()              // Permite TODOS os endpoints sob /auth (incluindo /auth/login)
                    .anyRequest().permitAll();                            // Permite qualquer outra requisição (mantendo a lógica original)
            });

        return http.build();
    }
}