package com.cooksys.mocktwitter.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.mocktwitter.dtos.HashtagDto;
import com.cooksys.mocktwitter.dtos.TweetResponseDto;
import com.cooksys.mocktwitter.services.HashtagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {
	
	private final HashtagService hashtagService;
	
	@GetMapping
	public List<HashtagDto> getAllHashtags() {
        return hashtagService.getAllHashtags();
    }
	
	@GetMapping(value = "/{label}")
	public List<TweetResponseDto> getTagsLabel(@PathVariable String label){
		return hashtagService.getTagsLabel(label);
	}

}