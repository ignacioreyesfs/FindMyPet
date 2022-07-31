package com.ireyes.findMyPet.dao.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ireyes.findMyPet.model.post.Post;

@Repository
public interface PostRepository<T extends Post> extends JpaRepository<T, Long>{
	public List<Post> findByPet_Breed_Name(String breedName);
}
