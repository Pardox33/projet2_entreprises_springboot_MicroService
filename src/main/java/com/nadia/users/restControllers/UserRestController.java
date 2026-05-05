package com.nadia.users.restControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.nadia.users.entities.User;
import com.nadia.users.service.UserService;
import com.nadia.users.service.register.RegistrationRequest;

@RestController
@CrossOrigin(origins = "*")
public class UserRestController {

    @Autowired UserService userService;

    @GetMapping("/all")
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/user/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    
    @PostMapping("/register")
    public User register(@RequestBody RegistrationRequest request) {
        return userService.registerUser(request);
    }

   
    @GetMapping("/verifyEmail/{token}")
    public User verifyEmail(@PathVariable("token") String token) {
        return userService.validateToken(token);
    }
}