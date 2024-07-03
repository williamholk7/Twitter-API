package com.cooksys.mocktwitter.services;

public interface ValidateService {

	boolean hashtagExists(String label);

	boolean usernameExists(String username);

	boolean usernameIsAvailable(String username);

}
