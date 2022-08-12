package com.ireyes.findMyPet.service.post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ireyes.findMyPet.dao.post.FoundRepository;
import com.ireyes.findMyPet.dao.post.PostRepository;
import com.ireyes.findMyPet.dao.post.SearchRepository;
import com.ireyes.findMyPet.model.post.Found;
import com.ireyes.findMyPet.model.post.Post;
import com.ireyes.findMyPet.model.post.PostFactory;
import com.ireyes.findMyPet.service.storage.StorageService;

@Service
public class PostService {
	@Autowired
	private PostRepository<Post> postRepo;
	@Autowired
	private SearchRepository searchRepo;
	@Autowired
	private FoundRepository foundRepo;
	@Autowired
	private StorageService storageService;
	
	@Transactional
	public List<Post> findAll(){
		return postRepo.findAll();
	}
	
	@Transactional
	public Page<Post> findAll(Pageable pagination){
		return postRepo.findAll(pagination);
	}
	
	@Transactional
	public Page<Post> findByPostCriteria(PostCriteria filter, Pageable pagination){
		Date dateFrom;
		try {
			dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse(filter.getDateFrom());
		} catch (ParseException e) {
			dateFrom = null;
		}
		
		if(filter.getPostType().equals("search")) {
			return searchRepo.findByFiltering(filter.getPetType(), filter.getBreed(), filter.getCountry(), 
					filter.getRegion(), filter.getSubRegion(), dateFrom, pagination);
		}
		
		if(filter.getPostType().equals("found")) {
			return foundRepo.findByFiltering(filter.getPetType(), filter.getBreed(), filter.getCountry(), 
					filter.getRegion(), filter.getSubRegion(), dateFrom, filter.getRelocation(), pagination);
		}
		
		return postRepo.findByFiltering(filter.getPetType(), filter.getBreed(), filter.getCountry(), 
				filter.getRegion(), filter.getSubRegion(), dateFrom, pagination);
	}
	
	@Transactional
	public Post save(PostDTO postDTO) {
		Post post = PostFactory.createPost(postDTO.getPostType());
		String filePrefix;
		
		post.setUser(postDTO.getUser());
		post.setPet(postDTO.getPet());
		post.setDescription(postDTO.getDescription());
		post.setLocation(postDTO.getLocation());
		post.setDate(postDTO.getDate());
		
		if(post instanceof Found) {
			((Found) post).setRelocationUrgency(postDTO.getRelocationUrgency());
		}
		
		post = postRepo.save(post);
		
		filePrefix = post.getUser().getId() + "_" + post.getId() + "_";
		
		storageService.store(postDTO.getImage(), filePrefix);
		
		post.getPet().addImageFilename(filePrefix + postDTO.getImage().getOriginalFilename());
		
		return postRepo.save(post);
	}
	
	@Transactional
	public void deleteById(Long id) {
		postRepo.deleteById(id);
	}

}
