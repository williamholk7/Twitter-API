package com.cooksys.mocktwitter.services;

import java.util.List;

import com.cooksys.mocktwitter.dtos.ContextDto;
import com.cooksys.mocktwitter.dtos.CredentialsDto;
import com.cooksys.mocktwitter.dtos.HashtagDto;
import com.cooksys.mocktwitter.dtos.TweetRequestDto;
import com.cooksys.mocktwitter.dtos.TweetResponseDto;
import com.cooksys.mocktwitter.dtos.UserResponseDto;
import com.cooksys.mocktwitter.entities.Credentials;

public interface TweetService {
	
	List<TweetResponseDto> getAllTweets();
	
	TweetResponseDto createTweet(String content, CredentialsDto credentials);
	
	TweetResponseDto createReply(Long id, String content, CredentialsDto credentials);
	
	TweetResponseDto createRepost(Long id, CredentialsDto credentials);
	
	ContextDto getContextById(Long id);
	
	TweetResponseDto getTweetById(Long id);
	
	TweetResponseDto deleteTweet(Long id, CredentialsDto credentials);
	
	List<HashtagDto> getTagsFromTweet(Long id);
	
	List<UserResponseDto> getMentionsFromTweet(Long id);

	List<TweetResponseDto> getRepliesByID(Long id);

	List<TweetResponseDto> getRepostsByID(Long id);

	List<UserResponseDto> getLikesFromTweet(Long id);

	void createLike(Long id, CredentialsDto credentials);

}
