package com.ireyes.findMyPet.database;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.ireyes.findMyPet.dao.UserRepository;
import com.ireyes.findMyPet.dao.post.FoundRepository;
import com.ireyes.findMyPet.dao.post.PostRepository;
import com.ireyes.findMyPet.dao.post.SearchRepository;
import com.ireyes.findMyPet.model.pet.Breed;
import com.ireyes.findMyPet.model.pet.Pet;
import com.ireyes.findMyPet.model.pet.PetType;
import com.ireyes.findMyPet.model.post.Found;
import com.ireyes.findMyPet.model.post.Post;
import com.ireyes.findMyPet.model.post.Search;
import com.ireyes.findMyPet.model.user.User;

@DataJpaTest
@ActiveProfiles("test")
public class PostTest {
	@Autowired
	private PostRepository<Post> postRepository;
	@Autowired
	private FoundRepository foundRepository;
	@Autowired
	private SearchRepository searchRepository;
	@Autowired
	private UserRepository userRepo;
	
	@BeforeEach
	public void init() {
		Calendar calendar = Calendar.getInstance();
		
		PetType dog = new PetType("dog");
		
		User user = userRepo.save(new User());
		
		Breed poodle = new Breed("poodle", dog);
		Breed caneCorso = new Breed("cane corso", dog);
		
		Pet tommy = new Pet("tommy", 1, poodle, "");
		Pet dora = new Pet("dora", 9, caneCorso, "");
		Pet etzio = new Pet("etzio", 7, poodle, "");
		Pet eco = new Pet("eco", 4, caneCorso, "");
		
		Found tommyFound = new Found();
		tommyFound.setPet(tommy);
		tommyFound.setUser(user);
		calendar.set(2022, Calendar.JANUARY, 1);
		tommyFound.setDate(calendar.getTime());
		Found doraFound = new Found();
		doraFound.setPet(dora);
		calendar.set(2022, Calendar.MARCH, 1);
		doraFound.setDate(calendar.getTime());
		doraFound.setUser(user);
		Found etzioFound = new Found();
		etzioFound.setPet(etzio);
		calendar.set(2022, Calendar.FEBRUARY, 1);
		etzioFound.setDate(calendar.getTime());
		etzioFound.setUser(user);
		Search ecoSearch = new Search();
		ecoSearch.setPet(eco);
		ecoSearch.setUser(user);
		calendar.set(2022, Calendar.JUNE, 1);
		ecoSearch.setDate(calendar.getTime());
		
		postRepository.save(tommyFound);
		postRepository.save(doraFound);
		postRepository.save(ecoSearch);
		postRepository.saveAndFlush(etzioFound);
	}
	
	@Test
	public void findByPostTypeTest() {
		assertThat(postRepository.findAll().size()).isEqualTo(4);
		assertThat(foundRepository.findAll().size()).isEqualTo(3);
		assertThat(searchRepository.findAll().size()).isEqualTo(1);
	}
	
	@Test
	public void findByBreedName() {		
		assertThat(foundRepository.findByPet_Breed_Name("poodle"))
				.allMatch(found -> found.getPet().getBreed().getName().equals("poodle"));
		assertThat(searchRepository.findByPet_Breed_Name("poodle").size()).isEqualTo(0);
	}
	
	@Test
	public void findSinceDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2022, Calendar.FEBRUARY, 1);
		assertThat(postRepository.findSinceDate(calendar.getTime()).size()).isEqualTo(3);
		calendar.set(2022, Calendar.FEBRUARY, 2);
		assertThat(postRepository.findSinceDate(calendar.getTime()).size()).isEqualTo(2);
	}
	
}
