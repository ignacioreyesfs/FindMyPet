package com.ireyes.findMyPet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ireyes.findMyPet.model.post.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>{
	@Query("SELECT DISTINCT(country) FROM #{#entityName}")
	public List<String> findAllCountries();
	
	@Query("SELECT DISTINCT(region) FROM #{#entityName} WHERE country LIKE :country")
	public List<String> findRegionsByCountry(@Param("country")String country);
	
	public List<Location> findByCountryAndRegion(String country, String region);
}
