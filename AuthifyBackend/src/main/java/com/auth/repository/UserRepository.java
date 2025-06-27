package com.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
	
	Optional<UserEntity> findByEmail(String email);
	
	Boolean existsByEmail(String email); 
	
	

}
