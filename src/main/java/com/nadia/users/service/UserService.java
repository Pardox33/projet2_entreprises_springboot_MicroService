package com.nadia.users.service;

import java.util.List;

import com.nadia.users.entities.Role;
import com.nadia.users.entities.User;
import com.nadia.users.service.register.RegistrationRequest;


public interface UserService {
	User saveUser(User user);
	User findUserByUsername (String username);
	Role addRole(Role role);
	User addRoleToUser(String username, String rolename);
	List<User>findAllUsers();
	User registerUser(RegistrationRequest request);
	User validateToken(String code); 
}
