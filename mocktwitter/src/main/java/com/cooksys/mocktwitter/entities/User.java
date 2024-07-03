package com.cooksys.mocktwitter.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name="user_table")
public class User {

	@Id
	@GeneratedValue
	private Long id;
	
	@Embedded
	private Credentials credentials;
	
	@Embedded
	private Profile profile;
	
	@CreationTimestamp
	private Timestamp joined;
	
	private boolean deleted = false;
	
	
	@OneToMany(mappedBy = "author")
	private List<Tweet> tweets = new ArrayList<Tweet>();
	
	@ManyToMany
	@JoinTable(name = "followers_following")
	private List<User> following = new ArrayList<User>();
	
	@ManyToMany(mappedBy = "following")
	private List<User> followers = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(
			name = "user_likes",
			joinColumns = @JoinColumn(name ="user_id"),
			inverseJoinColumns = @JoinColumn(name ="tweet_id")
			)
	private List<Tweet> tweetLikes = new ArrayList<>();
	
	@ManyToMany(mappedBy = "userMentions" )
	private List<Tweet> tweetMentions = new ArrayList<>();
	
}
