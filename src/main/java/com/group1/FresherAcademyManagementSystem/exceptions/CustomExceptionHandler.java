package com.group1.FresherAcademyManagementSystem.exceptions;

import com.group1.FresherAcademyManagementSystem.utils.DateUtil;
import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", "Bad Request");
        responseBody.put("timestamp", DateUtil.formatTimestamp(new Date()));
        responseBody.put("status", status.value());

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            responseBody.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(responseBody, headers, status);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", "Constraint violation");
        responseBody.put("timestamp", DateUtil.formatTimestamp(new Date()));
        responseBody.put("status", HttpStatus.NOT_ACCEPTABLE.value());
        responseBody.put("error", ex.getMessage().split(",\s+"));
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_ACCEPTABLE);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleSecurityException(Exception ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", "Unauthorized");
        responseBody.put("timestamp", DateUtil.formatTimestamp(new Date()));
        responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
        responseBody.put("error", "Invalid username or password");

        return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", ex.getMessage());
        responseBody.put("timestamp", DateUtil.formatTimestamp(new Date()));
        responseBody.put("status", HttpStatus.NOT_FOUND.value());
        responseBody.put("error", "Requested entity does not exist");
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", "Illegal Argument Exception");
        responseBody.put("timestamp", DateUtil.formatTimestamp(new Date()));
        responseBody.put("status", HttpStatus.NOT_ACCEPTABLE.value());
        responseBody.put("error", ex.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoElementException(NoSuchElementException ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", "No such element found");
        responseBody.put("timestamp", DateUtil.formatTimestamp(new Date()));
        responseBody.put("status", HttpStatus.NOT_FOUND.value());
        responseBody.put("error", ex.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnimplementException.class)
    public ResponseEntity<Object> handleUnimplementedException(UnimplementException ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", "Method not implemented");
        responseBody.put("timestamp", DateUtil.formatTimestamp(new Date()));
        responseBody.put("status", HttpStatus.NOT_IMPLEMENTED.value());
        responseBody.put("error", ex.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        Matcher m = Pattern.compile("(Detail:.*\\.)").matcher(ex.getMessage());
        var sql_message = m.find() ? m.group(0) : ex.getMessage();
        responseBody.put("message", "Data constraint violation");
        responseBody.put("timestamp", DateUtil.formatTimestamp(new Date()));
        responseBody.put("status", HttpStatus.FORBIDDEN.value());
        responseBody.put("error", sql_message);

        return new ResponseEntity<>(responseBody, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException(IOException ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", "System failed to write sheet file for export");
        responseBody.put("timestamp", DateUtil.formatTimestamp(new Date()));
        responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseBody.put("error", ex.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StatusMismatchException.class)
    public ResponseEntity<Object> handleStatusMismatchException(StatusMismatchException ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", "Status mismatch");
        responseBody.put("timestamp", DateUtil.formatTimestamp(new Date()));
        responseBody.put("status", HttpStatus.EXPECTATION_FAILED.value());
        responseBody.put("error", ex.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", "Forbidden");
        responseBody.put("timestamp", new Date());
        responseBody.put("status", HttpStatus.FORBIDDEN.value());
        responseBody.put("error", ex.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(StudentExistedException.class)
    public ResponseEntity<Object> handleStudentExistedException(StudentExistedException ex, WebRequest request) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", "Student Existed");
        responseBody.put("timestamp", new Date());
        responseBody.put("status", HttpStatusCode.valueOf(409));
        responseBody.put("error", ex.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatusCode.valueOf(409));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", "Null pointer exception");
        responseBody.put("timestamp", new Date());
        responseBody.put("status", HttpStatus.FORBIDDEN.value());
        responseBody.put("error", ex.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.FORBIDDEN);
    }
}
