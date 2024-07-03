package com.cooksys.mocktwitter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.mocktwitter.dtos.ProfileDto;
import com.cooksys.mocktwitter.entities.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
	
	ProfileDto entityToDto(Profile profile);
	
	Profile profileDtoToEntity(ProfileDto profileDto);
	
	List<ProfileDto> entitiesToDtos(List<Profile> profiles); 

}
