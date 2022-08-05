package com.ireyes.findMyPet.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ireyes.findMyPet.model.user.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	public Role findByName(String name);
}
