package com.cooksys.mocktwitter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.mocktwitter.dtos.HashtagDto;
import com.cooksys.mocktwitter.entities.Hashtag;


@Mapper(componentModel = "spring")
public interface HashtagMapper {
	
	HashtagDto entityToDto(Hashtag hashtag);
	
	List<HashtagDto> entitiesToDtos(List<Hashtag> hashtags); 

}
