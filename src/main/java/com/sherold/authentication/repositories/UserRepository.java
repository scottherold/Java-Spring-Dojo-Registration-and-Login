package com.sherold.authentication.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sherold.authentication.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	// Find user by email -- login
	User findByEmail(String email);
}
