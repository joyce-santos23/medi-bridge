package br.com.medibridge.medi_bridge.catalog.infra.web.handler;

import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.NotFoundException;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ValidationException;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.exception.DuplicateResourceException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.NOT_FOUND,
                "https://api.medibridge.com/errors/not-found",
                "Recurso não encontrado",
                ex.getMessage(),
                Map.of("path", request.getRequestURI())
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail handleForbidden(ForbiddenException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.FORBIDDEN,
                "https://api.medibridge.com/errors/forbidden",
                "Acesso proibido",
                ex.getMessage(),
                Map.of("path", request.getRequestURI())
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidation(ValidationException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.BAD_REQUEST,
                "https://api.medibridge.com/errors/bad-request",
                "Requisição inválida",
                ex.getMessage(),
                Map.of("path", request.getRequestURI())
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicateResource(DuplicateResourceException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.CONFLICT,
                "https://api.medibridge.com/errors/conflict",
                "Conflito de dados",
                ex.getMessage(),
                Map.of(
                        "path", request.getRequestURI(),
                        "field", ex.getFieldName()
                )
        );
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(org.springframework.dao.DataIntegrityViolationException ex, HttpServletRequest request) {
        String detail = "Conflito de integridade de dados no banco de dados.";
        String field = "unknown";
        String message = ex.getMostSpecificCause().getMessage();
        if (message != null) {
            // Extrai o campo dinamicamente do detalhe de erro do banco de dados (ex: Key (email)=(...) already exists)
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("Key \\(([^)]+)\\)=");
            java.util.regex.Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                field = matcher.group(1);
            }

            if (message.contains("uk_user_email")) {
                detail = "O email informado já está cadastrado.";
            } else if (message.contains("uk_user_professional_registration")) {
                detail = "O registro profissional informado para este conselho já está cadastrado.";
            } else if (message.contains("uk_hospital_cnpj")) {
                detail = "O CNPJ informado já está cadastrado.";
            } else if (message.contains("uk_hospital_cnes")) {
                detail = "O CNES informado já está cadastrado.";
            }
        }
        return ProblemDetailFactory.create(
                HttpStatus.CONFLICT,
                "https://api.medibridge.com/errors/conflict",
                "Conflito de dados",
                detail,
                Map.of(
                        "path", request.getRequestURI(),
                        "field", field
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Valor inválido",
                        (existing, replacement) -> existing
                ));

        return ProblemDetailFactory.create(
                HttpStatus.BAD_REQUEST,
                "https://api.medibridge.com/errors/validation",
                "Erro de validação",
                "Existem campos inválidos na requisição",
                Map.of(
                        "path", request.getRequestURI(),
                        "fields", fieldErrors
                )
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void ignoreWebAccessDeniedExceptions(AccessDeniedException ex) {
        throw ex;
    }

    @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
    public void ignoreMethodSecurityDeniedExceptions(org.springframework.security.authorization.AuthorizationDeniedException ex) {
        throw ex;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "https://api.medibridge.com/errors/internal-server-error",
                "Erro interno do servidor",
                ex.getMessage(),
                Map.of("path", request.getRequestURI())
        );
    }
}
