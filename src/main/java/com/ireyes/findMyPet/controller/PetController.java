package com.ireyes.findMyPet.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ireyes.findMyPet.model.pet.Breed;
import com.ireyes.findMyPet.model.pet.PetType;
import com.ireyes.findMyPet.service.PetService;
import com.ireyes.findMyPet.service.storage.StorageService;

@Controller
public class PetController {
	@Autowired
	private PetService petService;
	@Autowired
	private StorageService storageService;
	
	@GetMapping("/admin/petType/new")
	public String showCreatePetType(Model model) {
		model.addAttribute("petType", new PetType());
		return "petType-form";
	}
	
	@PostMapping("/admin/petType/new")
	public String createPetType(@ModelAttribute(name = "petType")PetType petType, BindingResult br) {
		if(br.hasErrors()) {
			return "petType-form";
		}
		
		petService.savePetType(petType);
		
		return "redirect:/admin";
	}
	
	@GetMapping("/admin/breed/new")
	public String showCreateBreed(Model model) {
		model.addAttribute("petTypes", petService.findAllPetTypes());
		model.addAttribute("breed", new Breed());
		return "breed-form";
	}
	
	@PostMapping("/admin/breed/new")
	public String createBreed(@ModelAttribute(name="breed")Breed breed, BindingResult br, Model model) {
		if(br.hasErrors()) {
			model.addAttribute("petTypes", petService.findAllPetTypes());
			return "breed-form";
		}
		
		petService.saveBreed(breed);
		
		return "redirect:/admin";
	}
	
	@GetMapping("/breeds")
	@ResponseBody
	public List<Breed> findBreeds(@RequestParam("petType")String petType){
		return petService.findBreedByPetType_Name(petType);
	}
	
	@GetMapping("/pet/image/{filename}")
	@ResponseBody
	public byte[] getPetImage(@PathVariable("filename") String filename) throws IOException {
		return storageService.loadAsResource(filename).getInputStream().readAllBytes();
	}
	
}
