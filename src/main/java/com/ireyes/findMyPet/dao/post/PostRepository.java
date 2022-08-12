package com.ireyes.findMyPet.dao.post;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ireyes.findMyPet.model.post.Post;

@Repository
public interface PostRepository<T extends Post> extends JpaRepository<T, Long>{
	public final String POST_FILTER_QUERY = "SELECT p FROM #{#entityName} p WHERE "
			+ "(:petType = '' OR p.pet.breed.petType.name = :petType) AND "
			+ "(:breed = '' OR p.pet.breed.name = :breed) AND "
			+ "(:country = '' OR p.location.country = :country) AND "
			+ "(:region = '' OR p.location.region = :region) AND "
			+ "(:subRegion = '' OR p.location.subRegion = :subRegion) AND "
			+ "(:dateFrom IS NULL OR p.date >= :dateFrom)";
	
	public List<Post> findByPet_Breed_Name(String breedName);
	
	@Query("select p from #{#entityName} p where p.date >= :date")
	public List<Post> findSinceDate(@Param("date") Date date);
	
	/**
	 * params are optional, if the param is empty (or null for no strings), it is considered absent.
	 */
	@Query(POST_FILTER_QUERY)
	public Page<Post> findByFiltering(@Param("petType")String petType, @Param("breed")String breed,
			@Param("country")String country, @Param("region")String region, @Param("subRegion")String subRegion,
			@Param("dateFrom")Date dateFrom, Pageable pageable);
}
