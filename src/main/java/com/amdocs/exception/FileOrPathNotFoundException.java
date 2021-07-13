package com.amdocs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileOrPathNotFoundException extends RuntimeException {
	public FileOrPathNotFoundException(String message) {
		super(message);
	}

	public FileOrPathNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
