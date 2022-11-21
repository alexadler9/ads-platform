package pro.sky.adsplatform.exception;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.NotContextException;

@RestControllerAdvice
public class ContentException extends RuntimeException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleException(NoContentException exception) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ErrorMessage(exception.getMessage()));
    }
}


