package com.cooksys.mocktwitter.dtos;

import java.sql.Timestamp;
import java.util.Optional;

import com.cooksys.mocktwitter.entities.Tweet;
import com.cooksys.mocktwitter.entities.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetResponseDto {

	private Long id;

	private UserResponseDto author;
	
	private Timestamp posted;
	
	private String content;
	
	private TweetResponseDto inReplyTo;
	
	private TweetResponseDto repostOf;
	
}
