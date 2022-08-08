package com.ireyes.findMyPet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ireyes.findMyPet.model.post.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>{

}
