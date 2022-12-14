package com.ireyes.findMyPet.service.post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ireyes.findMyPet.dao.post.FoundRepository;
import com.ireyes.findMyPet.dao.post.PostRepository;
import com.ireyes.findMyPet.dao.post.SearchRepository;
import com.ireyes.findMyPet.model.post.Found;
import com.ireyes.findMyPet.model.post.Post;
import com.ireyes.findMyPet.model.post.PostFactory;
import com.ireyes.findMyPet.model.post.Search;
import com.ireyes.findMyPet.model.user.User;
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
	public List<PostDTO> findAll(){
		return postRepo.findAll(getDefaultSort()).stream().map(this::getPostDTO).toList();
	}
	
	@Transactional
	public Page<PostDTO> findAll(int page, int pageSize){
		Pageable pagination = PageRequest.of(page, pageSize, getDefaultSort());
		return postRepo.findAll(pagination).map(this::getPostDTO);
	}
	
	@Transactional
	public Optional<PostDTO> findById(Long id){
		Optional<Post> post = postRepo.findById(id);
		
		if(post.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(getPostDTO(post.get()));
	}
	
	@Transactional
	public Page<PostDTO> findByPostCriteria(PostCriteria filter, int page, int pageSize){
		Date dateFrom;
		Page<Post> posts;
		Pageable pagination = PageRequest.of(page, pageSize, getDefaultSort());
		
		try {
			dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse(filter.getDateFrom());
		} catch (ParseException e) {
			dateFrom = null;
		}
		
		if(filter.getPostType().equals("search")) {
			posts = searchRepo.findByFiltering(filter.getPetType(), filter.getBreed(), filter.getCountry(), 
					filter.getRegion(), filter.getSubRegion(), dateFrom, pagination);
		}else if(filter.getPostType().equals("found")) {
			posts = foundRepo.findByFiltering(filter.getPetType(), filter.getBreed(), filter.getCountry(), 
					filter.getRegion(), filter.getSubRegion(), dateFrom, filter.getRelocation(), pagination);
		}else {
			posts = postRepo.findByFiltering(filter.getPetType(), filter.getBreed(), filter.getCountry(), 
					filter.getRegion(), filter.getSubRegion(), dateFrom, pagination);
		}
		
		return posts.map(this::getPostDTO);
	}
	
	private Sort getDefaultSort() {
		return Sort.by("date").descending();
	}
	
	@Transactional
	public PostDTO save(PostDTO postDTO) {
		Post post = PostFactory.createPost(postDTO.getPostType());
		String filePrefix;
		boolean isUpdate = postDTO.getId() != null;
		List<String> filenamesToDelete = new ArrayList<>();
		
		post.setId(postDTO.getId());
		post.setUser(postDTO.getUser());
		post.setPet(postDTO.getPet());
		post.setDescription(postDTO.getDescription());
		post.setLocation(postDTO.getLocation());
		post.setDate(postDTO.getDate());
		
		if(post instanceof Found found) {
			found.setRelocationUrgency(postDTO.getRelocationUrgency());
		}
		
		if(isUpdate && postDTO.getImages() != null) {
			filenamesToDelete = postRepo.findById(postDTO.getId()).orElseThrow().getPet().getImagesFilenames();
		}
		
		post = postRepo.save(post);
		
		filePrefix = post.getUser().getId() + "_" + post.getId() + "_" + System.currentTimeMillis() + "_";
		
		if(postDTO.getImages() != null) {
			List<String> imageFilenames = new ArrayList<>();
			for(MultipartFile image: postDTO.getImages()) {
				storageService.store(image, filePrefix);
				imageFilenames.add(filePrefix + image.getOriginalFilename());
			}
			post.getPet().setImagesFilenames(imageFilenames);
		}
		
		for(String filename: filenamesToDelete) {
			storageService.delete(filename);
		}
		
		post = postRepo.save(post);
		
		return getPostDTO(post);
	}
	
	@Transactional
	public void deleteById(Long id) {
		postRepo.findById(id).ifPresent(
			post -> post.getPet().getImagesFilenames().forEach(filename -> storageService.delete(filename)));
		
		postRepo.deleteById(id);
	}
	
	@Transactional
	public List<PostDTO> findByUsername(String username){
		return postRepo.findByUser_Username(username).stream().map(this::getPostDTO).toList();
	}
	
	
	private PostDTO getPostDTO(Post post) {
		PostDTO postDTO = new PostDTO();
		postDTO.setId(post.getId());
		postDTO.setPet(post.getPet());
		postDTO.setDescription(post.getDescription());
		postDTO.setLocation(post.getLocation());
		postDTO.setUser(post.getUser());
		postDTO.setDate(post.getDate());
		
		if(post instanceof Found found) {
			postDTO.setRelocationUrgency(found.getRelocationUrgency());
			postDTO.setPostType("found");
		}else if(post instanceof Search) {
			postDTO.setPostType("search");
		}
		
		return postDTO;
	}
	
	@Transactional
	public void deleteByUser(User user) {
		List<Post> posts = postRepo.findByUser(user);
		posts.forEach(post -> post.getPet().getImagesFilenames().forEach(filename -> storageService.delete(filename)));
		postRepo.deleteAll(posts);
	}

}
