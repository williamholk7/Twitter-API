package com.cooksys.mocktwitter.services;

import java.util.List;

import com.cooksys.mocktwitter.dtos.CredentialsDto;
import com.cooksys.mocktwitter.dtos.ProfileDto;
import com.cooksys.mocktwitter.dtos.TweetResponseDto;
import com.cooksys.mocktwitter.dtos.UserRequestDto;
import com.cooksys.mocktwitter.dtos.UserResponseDto;

public interface UserService {
	
	List<UserResponseDto> getAllUsers();

	List<TweetResponseDto> getUserTweets(String username);

	List<UserResponseDto> getFollowers(String username);

	UserResponseDto deleteUser(String username, CredentialsDto credentials);

	void unfollowUser(CredentialsDto credentials, String username);

	UserResponseDto getUser(String username);

	UserResponseDto createUser(CredentialsDto credentials, ProfileDto profile);

	List<TweetResponseDto> getUserMentions(String username);

	void followUser(CredentialsDto credentials, String username);

	List<TweetResponseDto> getUserFeed(String username);

	UserResponseDto updateUserProfile(CredentialsDto credentials, ProfileDto profile, String username);

	List<UserResponseDto> getFollowing(String username);

}
