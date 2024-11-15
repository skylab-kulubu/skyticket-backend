package com.skylab.skyticket.core.results;

import org.springframework.http.HttpStatus;

public class SuccessResult extends Result {

	public SuccessResult(String message, HttpStatus httpStatus) {
		super(true, message, httpStatus);
		
	}
	
	public SuccessResult(HttpStatus httpStatus) {
		super(true, httpStatus);
	}

}