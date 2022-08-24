package com.ireyes.findMyPet.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@GetMapping("/admin/petType/new")
	@Secured("ROLE_ADMIN")
	public String showCreatePetType(Model model) {
		model.addAttribute("petType", new PetType());
		return "petType-form";
	}
	
	@PostMapping("/admin/petType/new")
	@Secured("ROLE_ADMIN")
	public String createPetType(@ModelAttribute(name = "petType")PetType petType, BindingResult br) {
		if(br.hasErrors()) {
			return "petType-form";
		}
		
		petService.savePetType(petType);
		
		return "redirect:/admin";
	}
	
	@GetMapping("/admin/petType/delete")
	@Secured("ROLE_ADMIN")
	public String showDeletePetType(Model model) {
		model.addAttribute("petType", new PetType());
		model.addAttribute("petTypes", petService.findAllPetTypes());
		return "petType-delete";
	}
	
	@PostMapping("/admin/petType/delete")
	@Secured("ROLE_ADMIN")
	public String deletePetType(@ModelAttribute PetType petType, RedirectAttributes redirect) {
		try {
			petService.deletePetType(petType);
		}catch(DataIntegrityViolationException e) {
			logger.info("Cannot delete pet type in use " + petType.getId());
			redirect.addFlashAttribute("errorMessage", "Cannot delete pet type in use");
		}
		
		return "redirect:/admin";
	}
	
	@GetMapping("/admin/breed/new")
	@Secured("ROLE_ADMIN")
	public String showCreateBreed(Model model) {
		model.addAttribute("petTypes", petService.findAllPetTypes());
		model.addAttribute("breed", new Breed());
		return "breed-form";
	}
	
	@PostMapping("/admin/breed/new")
	@Secured("ROLE_ADMIN")
	public String createBreed(@ModelAttribute(name="breed")Breed breed, BindingResult br, Model model) {
		if(br.hasErrors()) {
			model.addAttribute("petTypes", petService.findAllPetTypes());
			return "breed-form";
		}
		
		petService.saveBreed(breed);
		
		return "redirect:/admin";
	}
	
	@GetMapping("/admin/breed/delete")
	@Secured("ROLE_ADMIN")
	public String showDeleteBreed(Model model) {
		model.addAttribute("petTypes", petService.findAllPetTypes());
		model.addAttribute("breed", new Breed());
		return "breed-delete";
	}
	
	@PostMapping("/admin/breed/delete")
	@Secured("ROLE_ADMIN")
	public String deleteBreed(@ModelAttribute Breed breed, RedirectAttributes redirect) {
		try {
			petService.deleteBreed(breed);
		}catch(DataIntegrityViolationException e) {
			logger.info("Cannot delete breed in use " + breed.getId());
			redirect.addFlashAttribute("errorMessage", "Cannot delete breed in use");
		}
		return "redirect:/admin";
	}
	
	@GetMapping("/breeds")
	@ResponseBody
	public List<Breed> findBreeds(@RequestParam("petType")String petType){
		return petService.findBreedByPetType_Name(petType);
	}
	
	@GetMapping(value = "/pet/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] getPetImage(@PathVariable("filename") String filename) throws IOException {
		return storageService.loadAsResource(filename).getInputStream().readAllBytes();
	}
	
}
