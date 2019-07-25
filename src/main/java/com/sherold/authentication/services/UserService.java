package com.sherold.authentication.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.sherold.authentication.models.User;
import com.sherold.authentication.repositories.UserRepository;

@Service
public class UserService {
	// <----- Attributes ----->
	// dependency injection
	private final UserRepository userRepository;

	// <----- Constructors ----->
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	// <----- Methods ----->
	// register user and has their password
	public User registerUser(User user) {
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashed);
		return userRepository.save(user);
	}
	
	// find user by email
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	// find user by id
	public User findUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
		} else {
			return null;
		}
	}
	
	// authenticate user
	public boolean authenticateUser(String email, String password) {
		// first find the user by email
		User user = userRepository.findByEmail(email);
		// if we can't find the email, return false
		if(user == null) {
			return false;
		} else {
			// if the password match, return true, else, return false
			if(BCrypt.checkpw(password,  user.getPassword())) {
				return true;
			} else {
				return false;
			}
		}
	}
}
