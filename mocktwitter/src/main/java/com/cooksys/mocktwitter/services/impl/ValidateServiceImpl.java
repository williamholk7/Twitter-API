package com.cooksys.mocktwitter.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.mocktwitter.entities.Hashtag;
import com.cooksys.mocktwitter.entities.User;
import com.cooksys.mocktwitter.exceptions.NotFoundException;
import com.cooksys.mocktwitter.mappers.HashtagMapper;
import com.cooksys.mocktwitter.repositories.HashtagRepository;
import com.cooksys.mocktwitter.repositories.UserRepository;
import com.cooksys.mocktwitter.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	
	private final HashtagRepository hashtagRepository;
	private final UserRepository userRepository;
	
	private boolean usernameIsTaken(String username) {
		if(username == null) {
			throw new NotFoundException("No username provided.");
		}
		
		Optional<User> userOptional = userRepository.findByCredentialsUsername(username);
		
		if(userOptional.isEmpty()) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean hashtagExists(String label) {
		
		if(label == null) {
			throw new NotFoundException("No label provided.");
		}
		
		
		Optional<Hashtag> hashtagOptional = hashtagRepository.findByLabel(label);
		if(hashtagOptional.isEmpty()) {
			return false;
		}
		
		return true;
		
	}

	@Override
	public boolean usernameExists(String username) {
		return (usernameIsTaken(username)) ? true: false;
	}

	@Override
	public boolean usernameIsAvailable(String username) {
		return (usernameIsTaken(username)) ? false: true;
	}

}
