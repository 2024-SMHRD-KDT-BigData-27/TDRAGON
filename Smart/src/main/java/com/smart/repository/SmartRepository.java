package com.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smart.entity.users;



@Repository
public interface SmartRepository extends JpaRepository<users, Long>{

	@Transactional
	@Query("select u from users u where u.user_email = :user_email and u.user_pw = :user_pw")
	public users login(@Param("user_email") String user_email, 
            @Param("user_pw") String user_pw);
	
	@Transactional
	@Query("select u from users u where u.user_email = :user_email")
	public users existsByEmail(@Param("user_email") String user_email);

	

}