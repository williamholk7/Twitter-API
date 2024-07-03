package com.cooksys.mocktwitter.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1144860126380509033L;
	
	private String message;

}
