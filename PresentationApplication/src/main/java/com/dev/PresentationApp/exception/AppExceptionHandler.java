package com.dev.PresentationApp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.HeadersBuilder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public HeadersBuilder<?> catchUserNotFoundException() {
		return ResponseEntity.notFound();
	}

	@ExceptionHandler(PresentationNotFoundException.class)
	public HeadersBuilder<?> catchPresentationNotFoundException() {
		return ResponseEntity.notFound();
	}

	@ExceptionHandler(RatingNotFoundException.class)
	public HeadersBuilder<?> catchRatingNotFoundException() {
		return ResponseEntity.notFound();
	}

}