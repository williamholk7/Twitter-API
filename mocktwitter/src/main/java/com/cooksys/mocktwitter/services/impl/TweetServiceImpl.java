package com.cooksys.mocktwitter.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.mocktwitter.dtos.ContextDto;
import com.cooksys.mocktwitter.dtos.CredentialsDto;
import com.cooksys.mocktwitter.dtos.HashtagDto;
import com.cooksys.mocktwitter.dtos.TweetRequestDto;
import com.cooksys.mocktwitter.dtos.TweetResponseDto;
import com.cooksys.mocktwitter.dtos.UserResponseDto;
import com.cooksys.mocktwitter.entities.Credentials;
import com.cooksys.mocktwitter.entities.Hashtag;
import com.cooksys.mocktwitter.entities.Tweet;
import com.cooksys.mocktwitter.entities.User;
import com.cooksys.mocktwitter.exceptions.BadRequestException;
import com.cooksys.mocktwitter.exceptions.NotAuthorizedException;
import com.cooksys.mocktwitter.exceptions.NotFoundException;
import com.cooksys.mocktwitter.mappers.CredentialsMapper;
import com.cooksys.mocktwitter.mappers.HashtagMapper;
import com.cooksys.mocktwitter.mappers.TweetMapper;
import com.cooksys.mocktwitter.mappers.UserMapper;
import com.cooksys.mocktwitter.repositories.HashtagRepository;
import com.cooksys.mocktwitter.repositories.TweetRepository;
import com.cooksys.mocktwitter.repositories.UserRepository;
import com.cooksys.mocktwitter.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService{
	
	private final TweetMapper tweetMapper;
	private final TweetRepository tweetRepository;
	private final UserRepository userRepository;
	private final CredentialsMapper credentialsMapper;
	private final HashtagMapper hashtagMapper;
	private final HashtagRepository hashtagRepository;
	private final UserMapper userMapper;
	
	private Tweet getTweet(Long id) {
        Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
        if (optionalTweet.isEmpty()) {
            throw new NotFoundException("No tweet with id: " + id);
        }
        return optionalTweet.get();
	}

	@Override
	public List<TweetResponseDto> getAllTweets(){
    	List<Tweet> tweets = tweetRepository.findAllByDeletedFalse();
    	Collections.reverse(tweets);
		return tweetMapper.entitiesToDtos(tweets);
	}
	
	@Override
	public TweetResponseDto getTweetById(Long id) {
        return tweetMapper.entityToDto(getTweet(id));
	}



	@Override
	public List<TweetResponseDto> getRepliesByID(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
        if (optionalTweet.isEmpty()) {
            throw new NotFoundException("No tweet with id: " + id);
        }
        Tweet tweet = optionalTweet.get();
        List<Tweet> replies = tweet.getReplies();
        List<Tweet> notDeletedReplies = new ArrayList<Tweet>();
        for(Tweet reply : replies) {
        	if(!reply.isDeleted()) {
        		notDeletedReplies.add(reply);
        	}
        }
        return tweetMapper.entitiesToDtos(notDeletedReplies);
	}


	@Override
	public List<TweetResponseDto> getRepostsByID(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
        if (optionalTweet.isEmpty()) {
            throw new NotFoundException("No tweet with id: " + id);
        }
        Tweet tweet = optionalTweet.get();
        List<Tweet> reposts = tweet.getReposts();
        List<Tweet> notDeletedReposts = new ArrayList<Tweet>();
        for(Tweet reply : reposts) {
        	if(!reply.isDeleted()) {
        		notDeletedReposts.add(reply);
        	}
        }
        return tweetMapper.entitiesToDtos(notDeletedReposts);
	}
	
	

		
	private Tweet contentParse(String content, Tweet tweet) {
		String[] arrOfStr = content.split(" ", -1);
		for (String s : arrOfStr){
			if(s.charAt(0) == '#') {
				s = s.substring(1);
				Optional<Hashtag> tag = hashtagRepository.findByLabel(s);
				if (tag.isEmpty()) {
					Hashtag tagToCreate = new Hashtag();
					tagToCreate.setLabel(s);
					List<Tweet> tweets = new ArrayList<Tweet>();
					tweets.add(tweet);
					tagToCreate.setHashtagTweets(tweets);
					Hashtag createdTag = hashtagRepository.saveAndFlush(tagToCreate);
					List<Hashtag> tags = tweet.getTweetHashtags();
					tags.add(createdTag);
					tweet.setTweetHashtags(tags);
				}
				else {
					Hashtag tagToUpdate = tag.get();
					List<Tweet> tweets = tagToUpdate.getHashtagTweets();
					tweets.add(tweet);
					tagToUpdate.setHashtagTweets(tweets);
					Hashtag updatedTag = hashtagRepository.saveAndFlush(tagToUpdate);
					List<Hashtag> tags = tweet.getTweetHashtags();
					tags.add(updatedTag);
					tweet.setTweetHashtags(tags);
				}
			}
			else if(s.charAt(0) == '@') {
				s = s.substring(1);
				Optional<User> user = userRepository.findByCredentialsUsernameAndDeletedIsFalse(s);
				if (!user.isEmpty()) {
					User userToUpdate = user.get();
					List<Tweet> tweetMentions = userToUpdate.getTweetMentions();
					tweetMentions.add(tweet);
					userToUpdate.setTweetMentions(tweetMentions);
					User updatedUser = userRepository.saveAndFlush(userToUpdate);
					List<User> userMentions = tweet.getUserMentions();
					userMentions.add(updatedUser);
					tweet.setUserMentions(userMentions);
				}
			}
		}
		
		return tweet;
	}
	
	
	@Override
	public TweetResponseDto createTweet(String content, CredentialsDto credentials) {
		if(content == null) {
			throw new BadRequestException("Bad Tweet Request!");
		}
		if(credentials == null || credentials.getUsername() == null || credentials.getPassword() == null) {
			throw new BadRequestException("Bad Credentials Request!");
		}
		
		Optional<User> user = userRepository.findByCredentialsUsername(credentials.getUsername());
		
		if (user.isEmpty()) {
			throw new BadRequestException("Bad Credentials Request!");
		}
		else {
			if(!user.get().getCredentials().getPassword().equals(credentials.getPassword())) {
				throw new BadRequestException("Bad Credentials Request!");
			}
			
		}	
		Tweet tweetToCreate = new Tweet();
		tweetToCreate.setContent(content);
		tweetToCreate.setAuthor(user.get());
		tweetToCreate.setDeleted(false);
		Tweet createdTweet = tweetRepository.saveAndFlush(tweetToCreate);
		tweetToCreate = contentParse(createdTweet.getContent(), createdTweet);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToCreate));
		
	}
	
	@Override
	public TweetResponseDto createReply(Long id, String content, CredentialsDto credentials) {
		Tweet originalTweet = getTweet(id);
		if(	originalTweet.isDeleted()) {
            throw new NotFoundException("No tweet with id: " + id);
		}
		if(content == null) {
			throw new BadRequestException("Bad Tweet Request!");
		}
		if(credentials == null || credentials.getUsername() == null || credentials.getPassword() == null) {
			throw new BadRequestException("Bad Credentials Request!");
		}
		
		Optional<User> user = userRepository.findByCredentialsUsername(credentials.getUsername());
		
		if (user.isEmpty()) {
			throw new BadRequestException("Bad Credentials Request!");
		}
		else {
			if(!user.get().getCredentials().getPassword().equals(credentials.getPassword())) {
				throw new BadRequestException("Bad Credentials Request!");
			}
		}	
		Tweet tweetToCreate = new Tweet();
		tweetToCreate.setContent(content);
		tweetToCreate.setAuthor(user.get());
		tweetToCreate.setDeleted(false);
		tweetToCreate.setInReplyTo(originalTweet);
		Tweet createdTweet = tweetRepository.saveAndFlush(tweetToCreate);
		tweetToCreate = contentParse(createdTweet.getContent(), createdTweet);
		tweetRepository.saveAndFlush(tweetToCreate);
		List<Tweet> originalTweetReplies = originalTweet.getReplies();
		originalTweetReplies.add(tweetToCreate);
		originalTweet.setReplies(originalTweetReplies);
		tweetRepository.saveAndFlush(originalTweet);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToCreate));
	}
	
	@Override
	public TweetResponseDto createRepost(Long id, CredentialsDto credentials) {
		Tweet originalTweet = getTweet(id);
		if(	originalTweet.isDeleted()) {
            throw new NotFoundException("No tweet with id: " + id);
		}

		if(credentials == null || credentials.getUsername() == null || credentials.getPassword() == null) {
			throw new BadRequestException("Bad Credentials Request! " + credentials);
		}
		
		Optional<User> user = userRepository.findByCredentialsUsername(credentials.getUsername());
		
		if (user.isEmpty()) {
			throw new BadRequestException("Bad Credentials Request!");
		}
		else {
			if(!user.get().getCredentials().getPassword().equals(credentials.getPassword())) {
				throw new BadRequestException("Bad Credentials Request!");
			}
		}	
		Tweet tweetToCreate = new Tweet();
		tweetToCreate.setAuthor(user.get());
		tweetToCreate.setDeleted(false);
		tweetToCreate.setRepostOf(originalTweet);
		//Tweet createdTweet = tweetRepository.saveAndFlush(tweetToCreate);
		tweetRepository.saveAndFlush(tweetToCreate);
		List<Tweet> originalTweetReposts = originalTweet.getReposts();
		originalTweetReposts.add(tweetToCreate);
		originalTweet.setReposts(originalTweetReposts);
		tweetRepository.saveAndFlush(originalTweet);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToCreate));
	}
	
	@Override
	public TweetResponseDto deleteTweet(Long id, CredentialsDto credentials) {
		Tweet tweetToDelete = getTweet(id);
		Credentials credentialsToCheck =  tweetToDelete.getAuthor().getCredentials();
		if (!credentials.equals(credentialsMapper.entityToDto(credentialsToCheck))) {
			throw new NotAuthorizedException("Your credentials do not match this tweet! " + credentials.getUsername());
		}
		tweetToDelete.setDeleted(true);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToDelete));
	}
	
	@Override
	public List<HashtagDto> getTagsFromTweet(Long id){
		Tweet tweet = getTweet(id);
		return hashtagMapper.entitiesToDtos(tweet.getTweetHashtags());
	}
	
	@Override
	public List<UserResponseDto> getMentionsFromTweet(Long id){
		Tweet tweet = getTweet(id);
		return userMapper.entitiesToDtos(tweet.getUserMentions());
	}

	@Override
	public List<UserResponseDto> getLikesFromTweet(Long id) {
		Tweet tweet = getTweet(id);
		List<User> usersWhoLiked = tweet.getUserLikes();
		List<User> notDeletedUsers = new ArrayList<User>();
		for(User user : usersWhoLiked) {
			if(!user.isDeleted()) {
				notDeletedUsers.add(user);
			}
		}
		return userMapper.entitiesToDtos(notDeletedUsers);
	}
	
	@Override
	public ContextDto getContextById(Long id) {
		Tweet tweet = getTweet(id);
		List<Tweet> after = new ArrayList();
		List<Tweet> before = new ArrayList();
		Tweet iterativeTweet = tweet;
		while (iterativeTweet.getInReplyTo() != null) {
			before.add(0, iterativeTweet.getInReplyTo());
			iterativeTweet = iterativeTweet.getInReplyTo();
		}
		for (Tweet t : tweet.getReplies()) {
			if (!t.isDeleted()) after.add(t);
		}
		ContextDto context = new ContextDto();
		context.setAfter(tweetMapper.entitiesToDtos(after));
		context.setBefore(tweetMapper.entitiesToDtos(before));
		context.setTarget(tweetMapper.entityToDto(tweet));
		return context;
	}

	@Override
	public void createLike(Long id, CredentialsDto credentials) {
		Tweet tweet = getTweet(id);
		Credentials creds = credentialsMapper.credentialsDtoToEntity(credentials);
		
		Optional<User> userOptional = userRepository.findByCredentialsUsernameAndDeletedIsFalse(creds.getUsername());
		if(userOptional.isEmpty()) {
			throw new NotFoundException("No user found by the username provided");
		}
		User user = userOptional.get();
		
		List<User> userLikes = tweet.getUserLikes();
		if(!userLikes.contains(user)){
			userLikes.add(user);
			tweet.setUserLikes(userLikes);
			tweetRepository.saveAndFlush(tweet);
			List<Tweet> tweetLikes = user.getTweetLikes();
			tweetLikes.add(tweet);
			user.setTweetLikes(tweetLikes);
			userRepository.saveAndFlush(user);

		}
		
	}

}
