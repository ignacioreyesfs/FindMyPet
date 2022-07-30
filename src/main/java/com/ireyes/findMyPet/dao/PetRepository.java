package com.ireyes.findMyPet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ireyes.findMyPet.model.pet.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long>{
	public List<Pet> findByName(String name);
	public List<Pet> findByBreedName(String breedName);
}
