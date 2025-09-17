package com.example.demo.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger Logger=LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(BlogNotFoundException.class)
	public ResponseEntity<String> handleNotFound(BlogNotFoundException ex){
		Logger.error("GLOBAL : Product not found exception {}", ex.getMessage(),ex);
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<String> handleNotFound(BadRequestException ex){
		Logger.error("GLOBAL : Bad Request exception {}", ex.getMessage(),ex);
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex){
		Logger.error("GLOBAL : Exception {}", ex.getMessage(),ex);
		return new ResponseEntity<>("internal server error"+ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
