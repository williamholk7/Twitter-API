package com.cooksys.mocktwitter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.mocktwitter.dtos.CredentialsDto;
import com.cooksys.mocktwitter.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
	
	CredentialsDto entityToDto(Credentials credentials);
	
	Credentials credentialsDtoToEntity(CredentialsDto credentialsDto);
	
	List<CredentialsDto> entitiesToDtos(List<Credentials> credentials); 

}
