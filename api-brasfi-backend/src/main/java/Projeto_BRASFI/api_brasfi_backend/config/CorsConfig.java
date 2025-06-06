package Projeto_BRASFI.api_brasfi_backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    private static final Logger logger = LoggerFactory.getLogger(CorsConfig.class);

    @Bean
    public CorsFilter corsFilter() {
        logger.info("Configurando CorsFilter");
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir as origens do frontend de desenvolvimento e produção
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173", "https://d1w8o5sk5gixl1.cloudfront.net"));
        
        // Permitir credenciais
        config.setAllowCredentials(true);
        
        // Permitir todos os métodos HTTP necessários
        config.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"
        ));
        
        // Permitir headers específicos
        config.setAllowedHeaders(Arrays.asList(
            "Origin",
            "Content-Type",
            "Accept",
            "Authorization",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "X-Requested-With"
        ));
        
        // Expor headers específicos
        config.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials",
            "Access-Control-Allow-Headers",
            "Access-Control-Allow-Methods",
            "Access-Control-Max-Age",
            "Authorization"
        ));
        
        // Cache do preflight por 1 hora
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        logger.info("CorsFilter configurado com sucesso");
        return new CorsFilter(source);
    }
} 