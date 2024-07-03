package com.cooksys.mocktwitter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.mocktwitter.dtos.TweetRequestDto;
import com.cooksys.mocktwitter.dtos.TweetResponseDto;
import com.cooksys.mocktwitter.entities.Tweet;


@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TweetMapper {

	TweetResponseDto entityToDto(Tweet tweet);
	
	Tweet requestToEntityDto(TweetRequestDto tweetRequestDto);
	
	List<TweetResponseDto> entitiesToDtos(List<Tweet> tweets); 
	
	
}
