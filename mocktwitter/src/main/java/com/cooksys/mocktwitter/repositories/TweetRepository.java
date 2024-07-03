package com.cooksys.mocktwitter.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.mocktwitter.entities.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
	

	List<Tweet> findByAuthorIdAndDeletedFalseOrderByPostedDesc(long authorId);
	
	List<Tweet> findByDeletedFalse();
	
	List<Tweet> findByContentContainingAndDeletedFalseOrderByPostedDesc(String substring);
	
	List<Tweet> findAllByDeletedFalse();
	
	Optional<Tweet> findByIdAndDeletedFalse(long Id);
	
	List<Tweet> findByAuthorId(long authorId);


}
