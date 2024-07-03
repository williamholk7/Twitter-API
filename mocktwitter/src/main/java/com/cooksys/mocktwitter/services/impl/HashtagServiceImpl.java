package com.cooksys.mocktwitter.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.mocktwitter.dtos.HashtagDto;
import com.cooksys.mocktwitter.dtos.TweetResponseDto;
import com.cooksys.mocktwitter.entities.Hashtag;
import com.cooksys.mocktwitter.entities.Tweet;
import com.cooksys.mocktwitter.exceptions.NotFoundException;
import com.cooksys.mocktwitter.mappers.HashtagMapper;
import com.cooksys.mocktwitter.mappers.TweetMapper;
import com.cooksys.mocktwitter.repositories.HashtagRepository;
import com.cooksys.mocktwitter.repositories.TweetRepository;
import com.cooksys.mocktwitter.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	
	private final HashtagMapper hashtagMapper;
	private final HashtagRepository hashtagRepository;
	private final TweetMapper tweetMapper;
	private final TweetRepository tweetRepository;

	@Override
	public List<HashtagDto> getAllHashtags() {
		
		List<Hashtag> hashtags = hashtagRepository.findAll();
		return hashtagMapper.entitiesToDtos(hashtags);
	}

	@Override
	public List<TweetResponseDto> getTagsLabel(String label) {
		Optional<Hashtag> hashtagOptional = hashtagRepository.findByLabel(label);
		if(hashtagOptional.isEmpty()) {
			throw new NotFoundException(label + " does not exist.");
		}
		
		List<Tweet> tweets = tweetRepository.findByContentContainingAndDeletedFalseOrderByPostedDesc(label);
		
		return tweetMapper.entitiesToDtos(tweets);
	}

}
