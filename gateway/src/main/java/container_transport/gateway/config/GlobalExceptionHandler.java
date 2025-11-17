package container_transport.gateway.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import container_transport.gateway.dto.exceptions.ErrorResponseDTO;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Handles ResponseStatusException (custom exceptions thrown in controllers)
     */
    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> handleResponseStatusException(
            ResponseStatusException ex,
            ServerWebExchange exchange) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                ex.getStatusCode().value(),
                ex.getStatusCode().toString(),
                ex.getReason() != null ? ex.getReason() : "An error occurred",
                exchange.getRequest().getPath().value()
        );

        return Mono.just(ResponseEntity
                .status(ex.getStatusCode())
                .body(error));
    }

    /**
     * Handles WebClientResponseException (errors from downstream microservices)
     */
    @ExceptionHandler(WebClientResponseException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> handleWebClientResponseException(
            WebClientResponseException ex,
            ServerWebExchange exchange) {

        ErrorResponseDTO error;
        
        // Try to parse the error response from the downstream service
        try {
            error = objectMapper.readValue(ex.getResponseBodyAsString(), ErrorResponseDTO.class);
            error.setPath(exchange.getRequest().getPath().value());
        } catch (JsonProcessingException e) {
            // If parsing fails, create a generic error response
            error = new ErrorResponseDTO(
                    ex.getStatusCode().value(),
                    ex.getStatusCode().toString(),
                    ex.getMessage(),
                    exchange.getRequest().getPath().value()
            );
        }

        return Mono.just(ResponseEntity
                .status(ex.getStatusCode())
                .body(error));
    }

    /**
     * Handles validation errors (from @Valid annotations)
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> handleValidationException(
            WebExchangeBindException ex,
            ServerWebExchange exchange) {

        List<ErrorResponseDTO.ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponseDTO.ValidationError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Invalid request data",
                exchange.getRequest().getPath().value()
        );
        error.setValidationErrors(validationErrors);

        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error));
    }

    /**
     * Handles IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> handleIllegalArgumentException(
            IllegalArgumentException ex,
            ServerWebExchange exchange) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );

        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error));
    }

    /**
     * Handles all other unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> handleGenericException(
            Exception ex,
            ServerWebExchange exchange) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred: " + ex.getMessage(),
                exchange.getRequest().getPath().value()
        );

        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error));
    }
}