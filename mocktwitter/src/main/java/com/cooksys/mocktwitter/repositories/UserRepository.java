package com.cooksys.mocktwitter.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cooksys.mocktwitter.entities.Credentials;
import com.cooksys.mocktwitter.entities.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByIdAndDeletedFalse(Long id);
	
	List<User> findAllByDeletedFalse();
	
	Optional<User> findByCredentialsUsernameAndDeletedIsFalse(String username);
	
	Optional<User> findByCredentialsUsername(String username);
	
	List<User> findByFollowersIdAndDeletedIsFalse(Long followersId);
	
	List<User> findByFollowingIdAndDeletedIsFalse(Long followingId);
	
	Optional<User> findByCredentialsAndDeletedIsFalse(Credentials credentials);
	
	

}
