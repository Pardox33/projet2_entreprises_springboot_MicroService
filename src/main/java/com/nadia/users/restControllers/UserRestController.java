package com.nadia.users.restControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nadia.users.entities.User;
import com.nadia.users.service.UserService;

@RestController
@CrossOrigin(origins ="*")
public class UserRestController {
	
	@Autowired
	UserService userService;
	
	//@RequestMapping(path="all",method= RequestMethod.GET)
	@GetMapping("all")
	public List<User> findAllUsers() {
		return userService.findAllUsers();
	}
	
	@GetMapping("/user/{username}")
	public User getUserByUsername(@PathVariable String username) {
	    return userService.findUserByUsername(username);
	}
}
