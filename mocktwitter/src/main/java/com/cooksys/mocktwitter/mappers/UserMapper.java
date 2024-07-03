package com.cooksys.mocktwitter.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.mocktwitter.dtos.UserRequestDto;
import com.cooksys.mocktwitter.dtos.UserResponseDto;
import com.cooksys.mocktwitter.entities.User;


@Mapper(componentModel = "spring", uses = {ProfileMapper.class, CredentialsMapper.class})
public interface UserMapper {
	@Mapping(target = "username", source = "credentials.username")
	UserResponseDto entityToDto(User user);
	
	User requestToEntityDto(UserRequestDto userRequestDto);
	
	List<UserResponseDto> entitiesToDtos(List<User> users); 

}
