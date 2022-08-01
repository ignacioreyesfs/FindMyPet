package com.ireyes.findMyPet.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ireyes.findMyPet.dao.PetRepository;
import com.ireyes.findMyPet.model.pet.Pet;

@Service
public class PetService {
	@Autowired
	private PetRepository petRepo;
	
	@Transactional
	public List<Pet> findAll(){
		return petRepo.findAll();
	}
	
	@Transactional
	public Optional<Pet> findById(Long id) {
		return petRepo.findById(id);
	}
	
	@Transactional
	public List<Pet> findByName(String name){
		return petRepo.findByName(name);
	}
	
	@Transactional
	public List<Pet> findByBreedName(String breedName){
		return petRepo.findByBreedName(breedName);
	}
	
	@Transactional
	public Pet save(Pet pet) {
		return petRepo.save(pet);
	}
	
	@Transactional
	public void deleteById(Long id) {
		petRepo.deleteById(id);
	}
	
}
