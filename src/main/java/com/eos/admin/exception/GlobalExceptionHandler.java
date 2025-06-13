package com.eos.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.eos.admin.error.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(DuplicateRecordException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateRecordException(DuplicateRecordException ex ,HttpServletRequest request) {
		ErrorResponse error = new ErrorResponse(
				ex.getMessage(),
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				request.getRequestURI()
				);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException ex,HttpServletRequest request){
		ErrorResponse error = new ErrorResponse(
				ex.getMessage(),
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				request.getRequestURI()
				);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	  @ExceptionHandler(ProcessException.class)
	    public ResponseEntity<ErrorResponse> handleProcessException(ProcessException ex ,HttpServletRequest request) {
		  ErrorResponse error = new ErrorResponse(
					ex.getMessage(),
					HttpStatus.BAD_REQUEST.value(),
					HttpStatus.BAD_REQUEST.getReasonPhrase(),
					request.getRequestURI()
					);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
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
	


}
