package learn.blogfiles.blog.handlers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFound404Exception.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundCases(NotFound404Exception notFoundException) {
        return ResponseEntity.status(NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .message(notFoundException.getMessage())
                        .statusCode(NOT_FOUND.value())
                        .build());
    }



}
