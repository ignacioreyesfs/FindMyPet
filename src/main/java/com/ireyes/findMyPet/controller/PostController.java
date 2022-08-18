package com.ireyes.findMyPet.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ireyes.findMyPet.model.post.RelocationUrgency;
import com.ireyes.findMyPet.service.LocationService;
import com.ireyes.findMyPet.service.PetService;
import com.ireyes.findMyPet.service.post.PostCriteria;
import com.ireyes.findMyPet.service.post.PostDTO;
import com.ireyes.findMyPet.service.post.PostService;
import com.ireyes.findMyPet.service.user.UserService;

@Controller
@RequestMapping("/posts")
public class PostController {
	@Autowired
	private PetService petService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private PostService postService;
	@Autowired
	private UserService userService;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	// list
	@GetMapping
	public String showPosts(Model model, @ModelAttribute("filter") PostCriteria filter, 
			@RequestParam(name="page", defaultValue="1")int page) {
		Page<PostDTO> pagePosts;
		Pageable pagination = PageRequest.of(page-1, 9);
		
		model.addAttribute("petTypes", petService.findAllPetTypes());
		model.addAttribute("countries", locationService.findAllCountries());
		model.addAttribute("relocations", Arrays.asList(RelocationUrgency.values()));
		model.addAttribute("filter", filter == null? new PostCriteria(): filter);
		
		if(filter != null) {
			pagePosts = postService.findByPostCriteria(filter, pagination);
		}else {
			pagePosts = postService.findAll(pagination);
		}
		
		model.addAttribute("posts", pagePosts.toList());
		model.addAttribute("currentPage", pagePosts.getNumber());
		model.addAttribute("pageNumbers", IntStream.rangeClosed(1, pagePosts.getTotalPages())
				.boxed().collect(Collectors.toList()));
		
		return "posts";
	}
	
	@GetMapping("/{id}")
	public String showPost(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
		PostDTO post = postService.findById(id).orElseThrow(ResourceNotFoundException::new);
		String referer = request.getHeader("Referer");
		
		model.addAttribute("post", post);
		model.addAttribute("backUrl", referer);
		
		return "post";
	}
	
	// creation
	@GetMapping("/new")
	public String showCreatePost(Model model, Principal principal) {
		PostDTO post = new PostDTO();
		
		if(principal == null) {
			throw new AccessDeniedException("Cannot create a post without user");
		}
		
		post.setUser(userService.findByUsername(principal.getName()).get());
		fillPostFormModel(post, "Create Post", model);
		
		return "post-form";
	}
	
	@PostMapping("/save")
	public String createPost(@Valid @ModelAttribute PostDTO post, BindingResult br, Model model,
			RedirectAttributes redirectAttr, Principal principal) {
		boolean isUpdate = post.getId() != null;
		
		if(isUpdate && !post.getUser().getUsername().equals(principal.getName())) {
			logger.info("access denied to user " + (principal != null? principal.getName(): "Anonymous"));
			throw new AccessDeniedException("Post is not from the current user");
		}
		
		if(br.hasErrors()) {
			logger.info("createPost binding results: " + br.toString());
			fillPostFormModel(post, isUpdate? "Update Post": "Create Post", model);
			if(isUpdate) {
				model.addAttribute("isUpdate", true);
			}
			model.addAttribute("paramsError", true);
			return "post-form";
		}
		
		if(isEmptyMultipartFiles(post.getImages())) {
			post.setImages(null);
		}
			
		post = postService.save(post);
		
		redirectAttr.addFlashAttribute(isUpdate? "updated": "created", true);
		
		return "redirect:/posts/" + post.getId();
	}
	
	private boolean isEmptyMultipartFiles(List<MultipartFile> files){
		return files.stream().filter(file -> !file.isEmpty() && !file.getOriginalFilename().equals("")).toList().isEmpty();
	}
	
	private void fillPostFormModel(PostDTO post, String title, Model model) {
		model.addAttribute("petTypes", petService.findAllPetTypes());
		model.addAttribute("countries", locationService.findAllCountries());
		model.addAttribute("relocationUrgencies", Arrays.asList(RelocationUrgency.values()));
		model.addAttribute("title", title);
		model.addAttribute("post", post);
	}
	
	@GetMapping("/edit/{id}")
	public String showUpdatePost(@PathVariable Long id, Model model, Principal principal) {
		PostDTO post = postService.findById(id).orElseThrow(ResourceNotFoundException::new);
		
		if(principal == null || !post.getUser().getUsername().equals(principal.getName())) {
			logger.info("access denied to user " + (principal != null? principal.getName(): "Anonymous"));
			throw new AccessDeniedException("Post is not from the current user");
		}
		
		model.addAttribute("isUpdate", true);
		fillPostFormModel(post, "Edit Post", model);
		
		return "post-form";
	}
	
	@PostMapping("/delete/{id}")
	public String deletePost(@PathVariable Long id, @RequestParam(required = false) String redirectUrl, Principal principal) {
		PostDTO post = postService.findById(id).orElseThrow();
		
		if(principal == null || !principal.getName().equals(post.getUser().getUsername())) {
			logger.severe("Invalid post delete request from " + (principal != null? principal.getName(): "Anon"));
			throw new AccessDeniedException("Invalid post delete request");
		}
		
		postService.deleteById(id);
		
		return "redirect:" + (redirectUrl != null? redirectUrl: "/");
	}
}
