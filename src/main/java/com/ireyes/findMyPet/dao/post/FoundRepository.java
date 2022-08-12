package com.ireyes.findMyPet.dao.post;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ireyes.findMyPet.model.post.Found;
import com.ireyes.findMyPet.model.post.Post;

@Repository
public interface FoundRepository extends PostRepository<Found>{
	
	/**
	 * params are optional, if the param is empty (or null for no strings), it is considered absent.
	 */
	@Query(POST_FILTER_QUERY + " AND (:relocation = '' OR p.relocationUrgency = :relocation)")
	public Page<Post> findByFiltering(@Param("petType")String petType, @Param("breed")String breed,
			@Param("country")String country, @Param("region")String region, @Param("subRegion")String subRegion,
			@Param("dateFrom")Date dateFrom, @Param("relocation") String relocation, Pageable pageable);
}
