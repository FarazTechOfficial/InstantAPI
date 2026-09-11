package {{package}}.exception;

import {{package}}.payload.ApiResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> handleNotFound(ResourceNotFoundException ex) {
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .success(true)
                .data(null)
                .build();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadApiException.class)
    public ResponseEntity<ApiResponseMessage> handleBadRequest(BadApiException ex) {
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .success(true)
                .data(null)
                .build();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
