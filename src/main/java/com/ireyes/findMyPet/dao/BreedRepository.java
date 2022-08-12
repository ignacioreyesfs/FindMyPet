package com.ireyes.findMyPet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ireyes.findMyPet.model.pet.Breed;
import com.ireyes.findMyPet.model.pet.PetType;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long>{
	public List<Breed> findBreedByPetType_Name(String petType);
}
