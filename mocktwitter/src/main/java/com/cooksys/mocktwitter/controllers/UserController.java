package com.cooksys.mocktwitter.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.mocktwitter.dtos.CredentialsDto;
import com.cooksys.mocktwitter.dtos.ProfileDto;
import com.cooksys.mocktwitter.dtos.TweetResponseDto;
import com.cooksys.mocktwitter.dtos.UserRequestDto;
import com.cooksys.mocktwitter.dtos.UserResponseDto;
import com.cooksys.mocktwitter.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	
	
	@GetMapping()
	public List<UserResponseDto> getAllUsers(){
		return userService.getAllUsers();
	}
	
	@GetMapping(value = "@{username}/tweets")
	public List<TweetResponseDto> getUserTweets(@PathVariable String username){		
		return userService.getUserTweets(username);
		
	}
	
	@GetMapping(value = "@{username}/followers")
	public List<UserResponseDto> getFollowers(@PathVariable String username){
		return userService.getFollowers(username);
	}
	
	@DeleteMapping(value = "@{username}")
	public UserResponseDto deleteUser( @RequestBody CredentialsDto credentials,@PathVariable String username) {
		return userService.deleteUser(username,credentials);
		
	}
	
	@PostMapping(value = "@{username}/unfollow")
	public void unfollowUser( @RequestBody CredentialsDto credentials,@PathVariable String username) {
		userService.unfollowUser(credentials,username);
	}
	
	@GetMapping(value = "@{username}")
	public UserResponseDto getUser(@PathVariable String username) {
		return userService.getUser(username);
	}
	
	@PostMapping
	public UserResponseDto createUser(@RequestBody UserRequestDto user) {
		return userService.createUser(user.getCredentials(),user.getProfile());
	}
	
	@GetMapping(value = "@{username}/mentions")
	public List<TweetResponseDto> getUserMentions(@PathVariable String username){
		return userService.getUserMentions(username);
	}
	
	@PostMapping(value = "@{username}/follow")
	public void followUser(@RequestBody CredentialsDto credentials,@PathVariable String username) {
		userService.followUser(credentials, username);
		
	}
	
	@GetMapping(value = "@{username}/feed")
	public List<TweetResponseDto> getUsersFeed(@PathVariable String username){
		return userService.getUserFeed(username);
	}
	
	@PatchMapping(value = "@{username}")
	public UserResponseDto updateUserProfile(@RequestBody UserRequestDto user,@PathVariable String username ) {
		return userService.updateUserProfile(user.getCredentials(),user.getProfile(),username);
	}
	
	@GetMapping(value = "@{username}/following")
	public List<UserResponseDto> getFollowing(@PathVariable String username){
		return userService.getFollowing(username);
	}
	
	

}
