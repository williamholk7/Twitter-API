package com.cooksys.mocktwitter.services;

import java.util.List;

import com.cooksys.mocktwitter.dtos.HashtagDto;
import com.cooksys.mocktwitter.dtos.TweetResponseDto;

public interface HashtagService {

	List<HashtagDto> getAllHashtags();

	List<TweetResponseDto> getTagsLabel(String label);

}
