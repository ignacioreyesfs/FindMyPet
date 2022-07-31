package com.ireyes.findMyPet.database;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ireyes.findMyPet.dao.post.FoundRepository;
import com.ireyes.findMyPet.dao.post.PostRepository;
import com.ireyes.findMyPet.dao.post.SearchRepository;
import com.ireyes.findMyPet.model.pet.Breed;
import com.ireyes.findMyPet.model.pet.Pet;
import com.ireyes.findMyPet.model.pet.PetType;
import com.ireyes.findMyPet.model.post.Found;
import com.ireyes.findMyPet.model.post.Post;
import com.ireyes.findMyPet.model.post.Search;

@DataJpaTest
public class PostTest {
	@Autowired
	private PostRepository<Post> postRepository;
	@Autowired
	private FoundRepository foundRepository;
	@Autowired
	private SearchRepository searchRepository;
	
	@Test
	public void findByPostTypeTest() {
		Found found = new Found();
		Search search = new Search();
		
		postRepository.save(found);
		postRepository.saveAndFlush(search);
		
		assertThat(postRepository.findAll().size()).isEqualTo(2);
		assertThat(foundRepository.findAll()).containsExactly(found);
		assertThat(searchRepository.findAll()).containsExactly(search);
	}
	
	@Test
	public void findByBreedName() {
		PetType dog = new PetType("dog");
		Breed poodle = new Breed("poodle", dog);
		Breed caneCorso = new Breed("cane corso", dog);
		Pet tommy = new Pet("tommy", 1, poodle, "");
		Pet dora = new Pet("dora", 9, caneCorso, "");
		Pet etzio = new Pet("etzio", 7, poodle, "");
		Found tommyFound = new Found();
		tommyFound.setPet(tommy);
		Found doraFound = new Found();
		doraFound.setPet(dora);
		Found etzioFound = new Found();
		etzioFound.setPet(etzio);
		
		postRepository.save(tommyFound);
		postRepository.save(doraFound);
		postRepository.saveAndFlush(etzioFound);
		
		assertThat(foundRepository.findByPet_Breed_Name("poodle")).containsExactly(tommyFound, etzioFound);
		assertThat(searchRepository.findByPet_Breed_Name("poodle").size()).isEqualTo(0);
	}
	
}
