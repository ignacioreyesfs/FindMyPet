package com.ireyes.findMyPet.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ireyes.findMyPet.model.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	@Query("SELECT DISTINCT(u) FROM User u JOIN Post p ON p.user.id = u.id "
			+ "WHERE p.date < :date")
	public List<User> findWithPostBefore(@Param("date") Date date);
	
	public Optional<User> findByUsername(String username);
	
	
	public boolean existsByUsername(String username);
}
