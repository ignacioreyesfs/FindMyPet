package com.ireyes.findMyPet.service.post;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ireyes.findMyPet.dao.post.FoundRepository;
import com.ireyes.findMyPet.dao.post.PostRepository;
import com.ireyes.findMyPet.dao.post.SearchRepository;
import com.ireyes.findMyPet.model.post.Found;
import com.ireyes.findMyPet.model.post.Post;
import com.ireyes.findMyPet.model.post.PostFactory;
import com.ireyes.findMyPet.model.post.Search;
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
		return postRepo.findAll().stream().map(post -> getPostDTO(post, false)).collect(Collectors.toList());
	}
	
	@Transactional
	public Page<PostDTO> findAll(Pageable pagination){
		return postRepo.findAll(pagination).map(post -> getPostDTO(post, false));
	}
	
	@Transactional
	public Optional<PostDTO> findById(Long id){
		Optional<Post> post = postRepo.findById(id);
		
		if(post.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(getPostDTO(post.get(), false));
	}
	
	@Transactional
	public Optional<PostDTO> findByIdWithMultiparts(Long id){
		Optional<Post> post = postRepo.findById(id);
		
		if(post.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(getPostDTO(post.get(), true));
	}
	
	@Transactional
	public Page<PostDTO> findByPostCriteria(PostCriteria filter, Pageable pagination){
		Date dateFrom;
		Page<Post> posts;
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
		
		return posts.map(post -> getPostDTO(post, false));
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
		
		for(MultipartFile image: postDTO.getImages()) {
			storageService.store(image, filePrefix);
			post.getPet().addImageFilename(filePrefix + image.getOriginalFilename());
		}
		
		return postRepo.save(post);
	}
	
	@Transactional
	public void deleteById(Long id) {
		postRepo.deleteById(id);
	}
	
	private PostDTO getPostDTO(Post post, boolean includeMultipart) {
		List<MultipartFile> images = new ArrayList<>();
		
		PostDTO postDTO = new PostDTO();
		postDTO.setId(post.getId());
		postDTO.setPet(post.getPet());
		postDTO.setDescription(post.getDescription());
		postDTO.setLocation(post.getLocation());
		postDTO.setUser(post.getUser());
		postDTO.setDate(post.getDate());
		
		if(post instanceof Found) {
			postDTO.setRelocationUrgency(((Found)post).getRelocationUrgency());
			postDTO.setPostType("found");
		}else if(post instanceof Search) {
			postDTO.setPostType("search");
		}
		
		if(includeMultipart) {
			for(String filename: post.getPet().getImagesFilenames()) {
				images.add(getMultipartFile(filename));
			}
		}
		
		postDTO.setImages(images);
		
		return postDTO;
	}
	
	private MultipartFile getMultipartFile(String filename) {
		File file = storageService.load(filename).toFile();
		FileItem fileItem;
		
		try (InputStream input = new FileInputStream(file)){
			fileItem = new DiskFileItem("image", Files.probeContentType(file.toPath()), 
					false, file.getName(), (int)file.length(), file.getParentFile());
			OutputStream  os = fileItem.getOutputStream();
			IOUtils.copy(input, os);
			IOUtils.closeQuietly(os);			
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return new CommonsMultipartFile(fileItem);
	}

}
