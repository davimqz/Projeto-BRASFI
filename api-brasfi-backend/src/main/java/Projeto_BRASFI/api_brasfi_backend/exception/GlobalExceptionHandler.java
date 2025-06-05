package Projeto_BRASFI.api_brasfi_backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        response.put("message", "Erro de validação");
        response.put("errors", errors);
        logger.warn("Erro de validação detectado: {}", errors, ex);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Requisição malformada ou ilegível.");
        // Para depuração, você pode querer logar a causa raiz, mas não exponha detalhes sensíveis ao cliente.
        // ex.getCause() pode dar mais informações.
        String detailedMessage = "O corpo da requisição não pôde ser lido. Verifique o formato do JSON.";
        if (ex.getCause() != null) {
            detailedMessage += " Causa: " + ex.getCause().getMessage();
        }
        errorResponse.put("message", detailedMessage);
        logger.warn("Erro de HttpMessageNotReadableException: {}", detailedMessage, ex);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Você pode adicionar outros handlers para exceções específicas aqui
    // Por exemplo, para EntityNotFoundException:
    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(jakarta.persistence.EntityNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Recurso não encontrado");
        error.put("message", ex.getMessage());
        logger.warn("Recurso não encontrado: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    // Handler genérico para outras exceções não tratadas (opcional, mas bom para debug)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erro interno do servidor");
        // CUIDADO: Não exponha ex.getMessage() diretamente em produção se puder conter informações sensíveis.
        error.put("message", "Ocorreu um erro inesperado no servidor. Tente novamente mais tarde."); 
        logger.error("Erro interno do servidor não tratado: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
} 