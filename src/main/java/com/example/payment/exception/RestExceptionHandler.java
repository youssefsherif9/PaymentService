package com.example.payment.exception;

import com.example.payment.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        // Extract validation error messages
        String messages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        // Build and return the error response
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed"+ messages, request);
    }


    @ExceptionHandler(PaymentValidationException.class)
    protected ResponseEntity<ErrorResponseDto> handleBusinessException(PaymentValidationException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred" + ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(HttpStatus status, String message, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setApiPath(request.getDescription(false));
        errorResponse.setErrorCode(status);
        errorResponse.setErrorMessage(message);
        errorResponse.setErrorTime(LocalDateTime.now());

        return ResponseEntity.status(status).body(errorResponse);
    }

}
