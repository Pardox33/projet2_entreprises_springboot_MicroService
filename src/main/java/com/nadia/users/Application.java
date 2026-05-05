package com.nadia.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nadia.users.service.UserService;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    UserService userService;

    /*
     * @jakarta.annotation.PostConstruct
     * void init_users() {
     * // Roles
     * userService.addRole(new Role(null, "ADMIN"));
     * userService.addRole(new Role(null, "USER"));
     * 
     * // Users
     * userService.saveUser(new User(null, "admin", "123", true));
     * userService.saveUser(new User(null, "nadia", "123", true));
     * userService.saveUser(new User(null, "yassine", "123", true));
     * 
     * // Assign roles
     * userService.addRoleToUser("admin", "ADMIN");
     * userService.addRoleToUser("admin", "USER");
     * userService.addRoleToUser("nadia", "USER");
     * userService.addRoleToUser("yassine", "USER");
     * }
     */
}