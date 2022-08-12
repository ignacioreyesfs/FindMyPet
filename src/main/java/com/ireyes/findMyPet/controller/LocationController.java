package com.ireyes.findMyPet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	@GetMapping("/regions")
	@ResponseBody
	public List<String> findRegions(@RequestParam String country){
		return locationService.findRegionsByCountry(country);
	}
	
	@GetMapping("/subregions")
	@ResponseBody // cambiar este para que retorne una location
	public List<Location> findSubRegions(@RequestParam String country,
			@RequestParam String region){
		return locationService.findByCountryAndRegion(country, region);
	}
}
