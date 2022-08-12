package com.ireyes.findMyPet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ireyes.findMyPet.dao.LocationRepository;
import com.ireyes.findMyPet.model.post.Location;

@Service
public class LocationService {
	@Autowired
	private LocationRepository locationRepo;
	
	public Location save(Location location) {
		location.setCountry(location.getCountry().toUpperCase());
		location.setRegion(location.getRegion().toUpperCase());
		location.setSubRegion(location.getSubRegion().toUpperCase());
		return locationRepo.save(location);
	}
	
	public List<String> findAllCountries(){
		return locationRepo.findAllCountries();
	}
	
	public List<String> findRegionsByCountry(String country){
		return locationRepo.findRegionsByCountry(country);
	}
	
	public List<Location> findByCountryAndRegion(String country, String region){
		return locationRepo.findByCountryAndRegion(country, region);
	}
}
