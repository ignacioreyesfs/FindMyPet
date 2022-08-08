package com.ireyes.findMyPet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ireyes.findMyPet.model.post.Location;
import com.ireyes.findMyPet.service.LocationService;

@Controller
public class LocationController {
	@Autowired
	private LocationService locationService;
	
	@GetMapping("/admin/location/new")
	public String showCreateLocation(Model model) {
		model.addAttribute("location", new Location());
		return "location-form";
	}
	
	@PostMapping("/admin/location/new")
	public String createLocation(@ModelAttribute(name="location")Location location, BindingResult br) {
		if(br.hasErrors()) {
			return "location-form";
		}
		
		locationService.save(location);
		
		return "redirect:/admin";
	}
}
