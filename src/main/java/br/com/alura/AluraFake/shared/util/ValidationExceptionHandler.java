package br.com.alura.AluraFake.shared.util;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;
import br.com.alura.AluraFake.shared.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ErrorItemDTO>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorItemDTO> errors = ex.getBindingResult().getFieldErrors().stream().map(ErrorItemDTO::new).toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorItemDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorItemDTO("resource", ex.getMessage()));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorItemDTO> handleDomainException(DomainException ex) {
        return ResponseEntity.badRequest().body(new ErrorItemDTO("domain", ex.getMessage()));
    }
}