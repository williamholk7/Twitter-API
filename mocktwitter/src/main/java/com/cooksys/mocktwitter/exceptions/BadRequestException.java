package com.cooksys.mocktwitter.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BadRequestException extends RuntimeException {
	
	private static final long serialVersionUID = 4418663419520889563L;
	
	private String message;
	
	

}
