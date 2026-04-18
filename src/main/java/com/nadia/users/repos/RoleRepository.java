package com.nadia.users.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nadia.users.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByRole(String role);
}
