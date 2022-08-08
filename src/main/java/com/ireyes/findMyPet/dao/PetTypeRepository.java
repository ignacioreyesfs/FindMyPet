package com.ireyes.findMyPet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ireyes.findMyPet.model.pet.PetType;

@Repository
public interface PetTypeRepository extends JpaRepository<PetType, Long>{
	
}
