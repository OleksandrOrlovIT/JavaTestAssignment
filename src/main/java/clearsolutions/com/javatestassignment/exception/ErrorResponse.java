package clearsolutions.com.javatestassignment.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
    private Map<String, String> errors;

    public ErrorResponse(HttpStatus status, LocalDateTime timestamp, String message, Map<String, String> errors) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.errors = errors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
