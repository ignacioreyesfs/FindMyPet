package com.ireyes.findMyPet.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.ireyes.findMyPet.dao.LocationRepository;
import com.ireyes.findMyPet.dao.UserRepository;
import com.ireyes.findMyPet.dao.post.PostRepository;
import com.ireyes.findMyPet.model.pet.Breed;
import com.ireyes.findMyPet.model.pet.Pet;
import com.ireyes.findMyPet.model.pet.PetType;
import com.ireyes.findMyPet.model.post.Location;
import com.ireyes.findMyPet.model.post.Post;
import com.ireyes.findMyPet.model.post.RelocationUrgency;
import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.service.post.PostDTO;
import com.ireyes.findMyPet.service.storage.StorageService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PostTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private PostRepository<Post> postRepo;
	@Autowired
	private LocationRepository locationRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private StorageService storageService;
	
	@Test
	public void showPostsTest() throws Exception {
		mvc.perform(get("/posts")).andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser("mockuser")
	public void submitPostTest() throws Exception{
		User user = createMockUser("mockuser");
		Location location = locationRepo.save(new Location("ARGENTINA", "CABA", "PALERMO"));
		MockMultipartFile image = new MockMultipartFile("images", "image1", "image/png", "test".getBytes());
		long postsNumber = postRepo.count();
		
		PostDTO postDTO = new PostDTO();
		postDTO.setPostType("found");
		postDTO.setPet(new Pet("eco", 1, new Breed("CANE CORSO", new PetType("DOG")), ""));
		postDTO.setLocation(location);
		postDTO.setUser(user);
		postDTO.setDate(Calendar.getInstance().getTime());
		postDTO.setRelocationUrgency(RelocationUrgency.NONE);
		postDTO.setImages(Arrays.asList(image));
		
		mvc.perform(post("/posts/save")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.flashAttr("postDTO", postDTO)
				.with(csrf()));
		
		assertThat(postRepo.count()).isEqualTo(postsNumber+1);
		
		storageService.deleteAll();
	}
	
	private User createMockUser(String username) {
		User user = new User();
		user.setEmail("mockuser@gmail.com");
		user.setUsername(username);
		user.setPassword("");
		
		return userRepo.save(user);
	}

}
