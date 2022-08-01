package com.ireyes.findMyPet.dao.post;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ireyes.findMyPet.model.post.Post;

@Repository
public interface PostRepository<T extends Post> extends JpaRepository<T, Long>{
	public List<Post> findByPet_Breed_Name(String breedName);
	@Query("select p from #{#entityName} p where p.date >= :date")
	public List<Post> findSinceDate(@Param("date") Date date);
}
