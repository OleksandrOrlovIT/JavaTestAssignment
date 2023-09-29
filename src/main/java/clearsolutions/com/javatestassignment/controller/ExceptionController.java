package clearsolutions.com.javatestassignment.controller;

import clearsolutions.com.javatestassignment.exception.ResourceNotFoundException;
import clearsolutions.com.javatestassignment.model.ErrorResponse;
import clearsolutions.com.javatestassignment.source.PropertySourceResolver;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    private final PropertySourceResolver propertyClass;

    public ExceptionController(PropertySourceResolver propertyClass) {
        this.propertyClass = propertyClass;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException() {
        Map<String, String> errors = new HashMap<>();
        errors.put("birthDate", "User age has to be more than " + propertyClass.getMinAge() + " years");

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), "Validation(Constraint) failed", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), "Validation failed", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<ErrorResponse> handleJpaSystemExceptions(JpaSystemException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("JpaSystemException", ex.getMessage() +
                " Please provide same id for user in body and in head of http request");
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), "JpaSystemException", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("IllegalArgumentException", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), "Illegal Argument", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();
        if(ex.getMessage().contains("DateTimeParseException") ||
                ex.getMessage().contains("Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDate")) {
            errors.put("DateTimeParseException", ex.getMessage() + ". Please put date in this format \"YYYY-MM-dd\"");
        }
        else
            errors.put("RuntimeException", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), "RuntimeException", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("ResourceNotFoundException", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, LocalDateTime.now(), "Resource not found", errors);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(DateTimeParseException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("DateTimeParseException", ex.getMessage() + " Please put date in this format \"YYYY-MM-dd\"");
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, LocalDateTime.now(), "DateTimeParseException", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
