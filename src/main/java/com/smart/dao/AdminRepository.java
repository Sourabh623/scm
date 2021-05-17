package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.smart.entities.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
	
	@Query("select u from Admin u where u.email= :email")
	public Admin getUserByUserName(@Param("email") String email);

}

