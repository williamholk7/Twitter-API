package com.cooksys.mocktwitter.dtos;

import java.util.Optional;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProfileDto {
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String phone;

}
