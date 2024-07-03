package com.cooksys.mocktwitter.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.mocktwitter.dtos.ContextDto;
import com.cooksys.mocktwitter.dtos.CredentialsDto;
import com.cooksys.mocktwitter.dtos.HashtagDto;
import com.cooksys.mocktwitter.dtos.TweetRequestDto;
import com.cooksys.mocktwitter.dtos.TweetResponseDto;
import com.cooksys.mocktwitter.dtos.UserResponseDto;
import com.cooksys.mocktwitter.entities.Credentials;
import com.cooksys.mocktwitter.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
	
	private final TweetService tweetService;
	
    @GetMapping
    public List<TweetResponseDto> getAllTweets() {
        return tweetService.getAllTweets();
    }
    
    @PostMapping
    public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
    	return tweetService.createTweet(tweetRequestDto.getContent(), tweetRequestDto.getCredentials());
    }
    
    @PostMapping("/{id}/reply")
    public TweetResponseDto createReply(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
    	return tweetService.createReply(id, tweetRequestDto.getContent(), tweetRequestDto.getCredentials());
    }
    
    @PostMapping("/{id}/repost")
    public TweetResponseDto createRepost(@PathVariable Long id, @RequestBody CredentialsDto credentials) {
    	return tweetService.createRepost(id, credentials);
    }
    
    
    @GetMapping("/{id}")
    public TweetResponseDto getTweetById(@PathVariable Long id) {
        return tweetService.getTweetById(id);
    }
    

    @GetMapping("/{id}/replies")
    public List<TweetResponseDto> getRepliesByID(@PathVariable Long id){
    	return tweetService.getRepliesByID(id);
    }
    
    @GetMapping("/{id}/reposts")
    public List<TweetResponseDto> getRepostsByID(@PathVariable Long id){
    	return tweetService.getRepostsByID(id);
    }
    
    @GetMapping("/{id}/context")
    public ContextDto getContextByID(@PathVariable Long id){
    	return tweetService.getContextById(id);
    }

    @DeleteMapping("/{id}")
    public TweetResponseDto deleteTweet(@PathVariable Long id, @RequestBody CredentialsDto credentials) {
    	return tweetService.deleteTweet(id, credentials);
    }
    
    @GetMapping("/{id}/tags")
    public List<HashtagDto> getTagsFromTweet(@PathVariable Long id){
    	return tweetService.getTagsFromTweet(id);
    }
    
    @GetMapping("/{id}/mentions")
    public List<UserResponseDto> getMentionsFromTweet(@PathVariable Long id){
    	return tweetService.getMentionsFromTweet(id);

    }
    
    @GetMapping("/{id}/likes")
    public List<UserResponseDto> getLikesFromTweet(@PathVariable Long id){
    	return tweetService.getLikesFromTweet(id);
    }
    
    @PostMapping("/{id}/like")
    public void createLike(@PathVariable Long id, @RequestBody CredentialsDto credentials) {
    	tweetService.createLike(id, credentials);
    }

}