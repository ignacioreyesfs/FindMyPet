package com.ireyes.findMyPet.service;

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
}
