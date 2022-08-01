package com.ireyes.findMyPet.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ireyes.findMyPet.dao.post.FoundRepository;
import com.ireyes.findMyPet.dao.post.PostRepository;
import com.ireyes.findMyPet.dao.post.SearchRepository;
import com.ireyes.findMyPet.model.post.Found;
import com.ireyes.findMyPet.model.post.Post;
import com.ireyes.findMyPet.model.post.Search;

@Service
public class PostService {
	@Autowired
	private PostRepository<Post> postRepo;
	@Autowired
	private SearchRepository searchRepo;
	@Autowired
	private FoundRepository foundRepo;
	
	@Transactional
	public List<Post> findAll(){
		return postRepo.findAll();
	}
	
	@Transactional
	public List<Search> finalAllSearches(){
		return searchRepo.findAll();
	}
	
	@Transactional
	public List<Found> finalAllFound(){
		return foundRepo.findAll();
	}
	
	@Transactional
	public Post save(Post post) {
		return postRepo.save(post);
	}
	
	@Transactional
	public void deleteById(Long id) {
		postRepo.deleteById(id);
	}

}
