package com.cooksys.mocktwitter.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException {
	
	private static final long serialVersionUID = -3752649622256679828L;
	
	private String message;

}
