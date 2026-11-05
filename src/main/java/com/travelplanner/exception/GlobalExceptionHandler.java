
package com.travelplanner.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404 - Resource does not exist
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex) {

        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    // 409 - Duplicate resource
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(
            DuplicateResourceException ex) {

        return createErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
    }

    // 401 - Invalid login credentials
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(
            InvalidCredentialsException ex) {

        return createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
        );
    }

    // 400 - Invalid business input
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex) {

        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    // Spring MVC errors:
    // 400, 404, 405, 406, 415, etc.
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String message = getClientErrorMessage(ex, status);

        if (status.is5xxServerError()) {
            log.error("Spring MVC server error", ex);
        }

        ErrorResponse response = buildErrorResponse(
                status,
                message
        );

        return new ResponseEntity<>(response, headers, status);
    }

    // Unexpected server errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception ex) {

        log.error("Unexpected server error", ex);

        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected server error occurred"
        );
    }

    private String getClientErrorMessage(
            Exception ex,
            HttpStatusCode status) {

        if (ex instanceof MethodArgumentNotValidException validationEx) {

            return validationEx.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .findFirst()
                    .map(error -> error.getDefaultMessage())
                    .orElse("Validation failed");
        }

        return switch (status.value()) {
            case 400 -> "Invalid request";
            case 401 -> "Unauthorized request";
            case 403 -> "Access denied";
            case 404 -> "Resource not found";
            case 405 -> "HTTP method not allowed for this endpoint";
            case 406 -> "Requested response format is not supported";
            case 409 -> "Resource conflict";
            case 415 -> "Unsupported media type";
            default -> status.is5xxServerError()
                    ? "An unexpected server error occurred"
                    : "Request could not be processed";
        };
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(
            HttpStatus status,
            String message) {

        return ResponseEntity.status(status)
                .body(buildErrorResponse(status, message));
    }

    private ErrorResponse buildErrorResponse(
            HttpStatusCode status,
            String message) {

        HttpStatus httpStatus = HttpStatus.resolve(status.value());

        String error = httpStatus != null
                ? httpStatus.getReasonPhrase()
                : "HTTP Error";

        return new ErrorResponse(
                status.value(),
                error,
                message,
                LocalDateTime.now()
        );
    }
}
