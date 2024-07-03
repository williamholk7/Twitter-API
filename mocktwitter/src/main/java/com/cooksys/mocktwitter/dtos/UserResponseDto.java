package com.cooksys.mocktwitter.dtos;

import java.sql.Timestamp;

import com.cooksys.mocktwitter.entities.Profile;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserResponseDto {
	
	private String username;
	
	private Profile profile;
	
	private Timestamp joined;

}
