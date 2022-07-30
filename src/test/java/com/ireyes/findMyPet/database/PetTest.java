package com.ireyes.findMyPet.database;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.ireyes.findMyPet.dao.PetRepository;
import com.ireyes.findMyPet.model.pet.Breed;
import com.ireyes.findMyPet.model.pet.Pet;
import com.ireyes.findMyPet.model.pet.PetType;

@DataJpaTest
public class PetTest {
	@Autowired
	private TestEntityManager em;
	@Autowired
	private PetRepository petRepository;
	
	@Test
	public void findByBreedNameTest() {
		PetType dog = new PetType("dog");
		Breed husky = new Breed("husky", dog);
		Breed beagle = new Breed("beagle", dog);
		
		Pet tommy = new Pet("tommy", 1, husky, "");
		Pet eco = new Pet("eco", 4, beagle, "");
		Pet sally = new Pet("sally", 0, beagle, "");
		
		em.persist(tommy);
		em.persist(eco);
		em.persist(sally);
		em.flush();
		
		List<Pet> beagles = petRepository.findByBreedName("beagle");
		
		assertThat(beagles.size()).isEqualTo(2);
		assertThat(beagles).contains(eco, sally);
	}

}
