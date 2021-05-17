package com.smart.dao;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;
import com.smart.entities.User;



public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	//pagination
	
	//this method return the list of contacts a particular user
	@Query("from Contact as c where c.user.id=:userId")
	public Page<Contact> getContactsByUser(@Param("userId") int userId, Pageable pePageable);
	
	//search method
	public List<Contact> findByNameContainingAndUser(String name, User user);
	
	
}
