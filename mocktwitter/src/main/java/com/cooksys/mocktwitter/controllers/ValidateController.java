package com.cooksys.mocktwitter.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.mocktwitter.services.ValidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {
	
	private final ValidateService validateService;
	
	@GetMapping(value = "/tag/exists/{label}")
	public boolean hashtagExists(@PathVariable String label) {
		return validateService.hashtagExists(label);
	}
	
	@GetMapping(value = "/username/exists/@{username}")
	public boolean usernameExists(@PathVariable String username) {
		return validateService.usernameExists(username);
	}
	
	@GetMapping(value = "/username/available/@{username}")
	public boolean usernameIsAvailable(@PathVariable String username) {
		return validateService.usernameIsAvailable(username);
	}

}