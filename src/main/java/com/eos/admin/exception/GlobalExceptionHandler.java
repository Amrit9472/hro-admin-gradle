package com.eos.admin.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.eos.admin.error.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
//	@ExceptionHandler(DuplicateRecordException.class)
//	public ResponseEntity<String> handleDuplicateRecordException(DuplicateRecordException ex) {
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//	}
	
	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<String> handleInvalidInputException(InvalidInputException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicateRecordException.class)
	public ResponseEntity<Map<String, String>> handleDuplicateEmail(DuplicateRecordException ex) {
	    Map<String, String> error = new HashMap();
	    error.put("message", ex.getMessage());
	    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
