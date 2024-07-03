package com.cooksys.mocktwitter.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.mocktwitter.dtos.CredentialsDto;
import com.cooksys.mocktwitter.dtos.ProfileDto;
import com.cooksys.mocktwitter.dtos.TweetResponseDto;
import com.cooksys.mocktwitter.dtos.UserResponseDto;
import com.cooksys.mocktwitter.entities.Credentials;
import com.cooksys.mocktwitter.entities.Tweet;
import com.cooksys.mocktwitter.entities.User;
import com.cooksys.mocktwitter.exceptions.BadRequestException;
import com.cooksys.mocktwitter.exceptions.NotAuthorizedException;
import com.cooksys.mocktwitter.exceptions.NotFoundException;
import com.cooksys.mocktwitter.mappers.CredentialsMapper;
import com.cooksys.mocktwitter.mappers.ProfileMapper;
import com.cooksys.mocktwitter.mappers.TweetMapper;
import com.cooksys.mocktwitter.mappers.UserMapper;
import com.cooksys.mocktwitter.repositories.TweetRepository;
import com.cooksys.mocktwitter.repositories.UserRepository;
import com.cooksys.mocktwitter.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final UserMapper userMapper;
	private final TweetMapper tweetMapper;
	private final CredentialsMapper credentialsMapper;
	private final ProfileMapper profileMapper;
	private final UserRepository userRepository;
	private final TweetRepository tweetRepository;

	
	private User getUserByUsername(String username) {
		if(username == null) {
			throw new NotFoundException("No user found by the username provided");
		}
		
		Optional<User> userOptional = userRepository.findByCredentialsUsernameAndDeletedIsFalse(username);
		if(userOptional.isEmpty()) {
			throw new NotFoundException("No user found by the username provided");
		}
		return userOptional.get();
		
	}
	
	@Override
	public List<UserResponseDto> getAllUsers(){
		List<User> users = userRepository.findAllByDeletedFalse();
		if (users == null) users = new ArrayList<User>();
;		return userMapper.entitiesToDtos(users);
	}
	
	@Override
	public List<TweetResponseDto> getUserTweets(String username) {				

		User foundUser = getUserByUsername(username);
		return tweetMapper.entitiesToDtos(tweetRepository.findByAuthorIdAndDeletedFalseOrderByPostedDesc(foundUser.getId()));
		
	}

	@Override
	public List<UserResponseDto> getFollowers(String username) {

		User foundUser = getUserByUsername(username);
		List<User> followers = userRepository.findByFollowersIdAndDeletedIsFalse(foundUser.getId());
		return userMapper.entitiesToDtos(followers);
		
		
	}

	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentials) {

		User foundUser = getUserByUsername(username);
		Credentials userCredentials = credentialsMapper.credentialsDtoToEntity(credentials);
		if(foundUser.getCredentials().equals(userCredentials)) {
			
			foundUser.setDeleted(true);
			userRepository.saveAndFlush(foundUser);
			return userMapper.entityToDto(foundUser);		
		}
		else {
			throw new NotAuthorizedException("Credentials do not match");
		}
		
		
		
	}
	
	private User getUserByCredentials(Credentials credentials) {
		
		Optional<User> userOptional = userRepository.findByCredentialsAndDeletedIsFalse(credentials);
		if(userOptional.isEmpty()) {
			throw new NotAuthorizedException("Incorrect credentials");
		}
		return userOptional.get();
		
	}

	@Override
	public void unfollowUser(CredentialsDto credentials, String username) {

		User foundUser = getUserByUsername(username);
		User credentialUser = getUserByCredentials(credentialsMapper.credentialsDtoToEntity(credentials));
		List<User> followers = userRepository.findByFollowersIdAndDeletedIsFalse(foundUser.getId());
		
		if(followers.contains(credentialUser)) {
			foundUser.getFollowing().remove(credentialUser);
			userRepository.save(foundUser);
						
		}
		else {
			throw new NotFoundException("This user is not following the other user");
		}
		
		
		
	}

	@Override
	public UserResponseDto getUser(String username) {
		
		return userMapper.entityToDto(getUserByUsername(username));
		
	}

	@Override
	public UserResponseDto createUser(CredentialsDto credentials, ProfileDto profile) {
		if(credentials == null ||profile == null|| credentials.getUsername() == null || credentials.getPassword() == null || profile.getEmail() == null) {
			throw new BadRequestException("Invalid fields for creating a user");
		}
		Optional<User> possibleExistingUser = userRepository.findByCredentialsUsername(credentials.getUsername());
		if(possibleExistingUser.isEmpty()) {
			User newUser = new User();
			newUser.setCredentials(credentialsMapper.credentialsDtoToEntity(credentials));
			newUser.setProfile(profileMapper.profileDtoToEntity(profile));
			newUser = userRepository.save(newUser);
			return userMapper.entityToDto(newUser);			
		}
		else {
			User existingUser = possibleExistingUser.get();
			if(existingUser.isDeleted()) {
				existingUser.setDeleted(false);
				existingUser = userRepository.save(existingUser);
				return userMapper.entityToDto(existingUser);
			}
			else {
				throw new BadRequestException("User already exists");
			}
		}
	}

	@Override
	public List<TweetResponseDto> getUserMentions(String username) {
		User foundUser = getUserByUsername(username);
		List<Tweet> tweets = tweetRepository.findByContentContainingAndDeletedFalseOrderByPostedDesc("@" + username);
		
		return tweetMapper.entitiesToDtos(tweets);
	}

	@Override
	public void followUser(CredentialsDto credentials, String username) {
		User foundUser = getUserByUsername(username);
		User credentialUser = getUserByCredentials(credentialsMapper.credentialsDtoToEntity(credentials));
		List<User> followers = userRepository.findByFollowersIdAndDeletedIsFalse(foundUser.getId());
		
		if(!followers.contains(credentialUser)) {
			foundUser.getFollowing().add(credentialUser);
			userRepository.save(foundUser);						
		}
		else {
			throw new BadRequestException("This user is already following the other user");
		}
		
		
		
	}

	@Override
	public List<TweetResponseDto> getUserFeed(String username) {
		User foundUser = getUserByUsername(username);
		List<Tweet> tweets = tweetRepository.findByAuthorIdAndDeletedFalseOrderByPostedDesc(foundUser.getId());
		List<User> following = foundUser.getFollowing();
		
		for(User user: following) {
			List<Tweet> userTweets = tweetRepository.findByAuthorIdAndDeletedFalseOrderByPostedDesc(user.getId());
			tweets.addAll(userTweets);
		}
		tweets.sort(Comparator.comparing(Tweet::getPosted).reversed());
		
		return tweetMapper.entitiesToDtos(tweets);
	}

	@Override
	public UserResponseDto updateUserProfile(CredentialsDto credentials, ProfileDto profile, String username) {
		if(credentials == null || profile == null|| credentials.getUsername() == null || credentials.getPassword() == null) {
			throw new NotAuthorizedException("Invalid Credentials");
		}
		User foundUser = getUserByUsername(username);
		if(foundUser.getCredentials().equals(credentialsMapper.credentialsDtoToEntity(credentials))) {
			if(profile != null & profile.getEmail() != null) {
			foundUser.setProfile(profileMapper.profileDtoToEntity(profile));
			}
			foundUser = userRepository.save(foundUser);
			return userMapper.entityToDto(foundUser);
		}
		else {
			throw new NotAuthorizedException("Invalid Credentials");
		}
	}

	@Override
	public List<UserResponseDto> getFollowing(String username) {
		User foundUser = getUserByUsername(username);
		List<User> following = userRepository.findByFollowingIdAndDeletedIsFalse(foundUser.getId());
		return userMapper.entitiesToDtos(following);
	}

}
