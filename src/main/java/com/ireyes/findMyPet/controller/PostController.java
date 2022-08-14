package com.ireyes.findMyPet.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ireyes.findMyPet.model.post.Post;
import com.ireyes.findMyPet.model.post.RelocationUrgency;
import com.ireyes.findMyPet.service.LocationService;
import com.ireyes.findMyPet.service.PetService;
import com.ireyes.findMyPet.service.post.PostDTO;
import com.ireyes.findMyPet.service.post.PostService;
import com.ireyes.findMyPet.service.post.PostCriteria;
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
		model.addAttribute("filter", filter == null? new PostCriteria(): filter);
		model.addAttribute("relocations", Arrays.asList(RelocationUrgency.values()));
		
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
	public String showPost(@PathVariable("id") Long id, Model model) {
		PostDTO post = postService.findById(id).orElseThrow(ResourceNotFoundException::new);
		
		model.addAttribute("post", post);
		
		return "post";
	}
	
	// creation
	@GetMapping("/new")
	public String showCreatePost(Model model, Principal principal) {
		if(principal == null) {
			throw new AccessDeniedException("Cannot create a post without user");
		}
		
		PostDTO post = new PostDTO();
		post.setUser(userService.findByUsername(principal.getName()).get());
		fillPostFormModel(post, model);
		
		return "post-form";
	}
	
	@PostMapping("/new")
	public String createPost(@Valid @ModelAttribute PostDTO post, BindingResult br, Model model,
			RedirectAttributes redirectAttr) {
		if(br.hasErrors()) {
			logger.info("createPost binding results: " + br.toString());
			fillPostFormModel(post, model);
			model.addAttribute("creationError", true);
			return "post-form";
		}
		
		postService.save(post);
		redirectAttr.addFlashAttribute("postCreated", true);
		
		return "redirect:/posts";
	}
	
	private void fillPostFormModel(PostDTO post, Model model) {
		model.addAttribute("petTypes", petService.findAllPetTypes());
		model.addAttribute("countries", locationService.findAllCountries());
		model.addAttribute("relocationUrgencies", Arrays.asList(RelocationUrgency.values()));
		model.addAttribute("post", post);
	}
	
}
