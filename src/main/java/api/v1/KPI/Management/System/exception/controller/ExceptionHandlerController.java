package api.v1.KPI.Management.System.exception.controller;

import api.v1.KPI.Management.System.exception.exps.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(ConfirmCodeExpiredException.class)
    public ResponseEntity<Object> handleConfirmCodeExpiredException(ConfirmCodeExpiredException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); //400
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handle(ResourceNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)  // 404
                .body(e.getMessage());
    }
    @ExceptionHandler(ProfileStatusException.class)
    public ResponseEntity<String> handle(ProfileStatusException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)  // 403
                .body(e.getMessage());
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<String> handle(ResourceConflictException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)   // 409
                .body(e.getMessage());
    }
    @ExceptionHandler(AttemptLimitException.class)
    public ResponseEntity<String> handle(AttemptLimitException e){
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS) // 429
                .body(e.getMessage());
    }

    @ExceptionHandler(AppBadException.class)
    public ResponseEntity<String> handle(AppBadException e){
        return ResponseEntity.badRequest().body(e.getMessage());    //  400
    }


    // Spring Security method-level access denied
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthorizationDenied(AuthorizationDeniedException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("error", "Forbidden");
        body.put("message", "Access Denied");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body); // 403
    }

    // Token yo‘q yoki noto‘g‘ri
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuth(AuthenticationException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("error", "Unauthorized");
        body.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body); // 401
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handle(RuntimeException e){
        e.printStackTrace();
        Map<String, Object> errorBody = new LinkedHashMap<>();
        errorBody.put("timestamp", new Date());
        errorBody.put("error", "Internal Server Error");
        errorBody.put("message", e.getMessage());
        return ResponseEntity.internalServerError().body(errorBody);    // 500
    }


}
