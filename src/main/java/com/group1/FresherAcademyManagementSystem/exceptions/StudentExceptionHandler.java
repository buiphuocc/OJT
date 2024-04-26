package com.group1.FresherAcademyManagementSystem.exceptions;

import com.group1.FresherAcademyManagementSystem.utils.DateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class StudentExceptionHandler {
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Object> handleStudentNotFoundException(StudentNotFoundException ex, WebRequest request){
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", ex.getMessage());
        responseBody.put("timestamp", DateUtil.formatTimestamp(new Date()));
        responseBody.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClassEntityNotFoundException.class)
    public ResponseEntity<Object> handleClassNotFoundException(ClassEntityNotFoundException ex, WebRequest request){
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("message", ex.getMessage());
        responseBody.put("timestamp", DateUtil.formatTimestamp(new Date()));
        responseBody.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorObject> handleStudentNotFoundException(NotFoundException ex, WebRequest request){
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StudentExistedException.class)
    public ResponseEntity<ErrorObject> handleStudentExistedException(StudentExistedException ex, WebRequest request){
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(409);
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatusCode.valueOf(409));
    }

}
