package com.ireyes.findMyPet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ireyes.findMyPet.model.pet.Breed;
import com.ireyes.findMyPet.model.pet.PetType;
import com.ireyes.findMyPet.service.PetService;

@Controller
public class PetController {
	@Autowired
	private PetService petService;
	
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
	
}
