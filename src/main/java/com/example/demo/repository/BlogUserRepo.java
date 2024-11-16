package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.BlogUser;

public interface BlogUserRepo extends JpaRepository<BlogUser, Integer>{
	
	
	BlogUser findByUsername(String username);
	

}
