package com.nadia.users.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nadia.users.entities.Role;
import com.nadia.users.entities.User;
import com.nadia.users.entities.VerificationToken;
import com.nadia.users.repos.RoleRepository;
import com.nadia.users.repos.UserRepository;
import com.nadia.users.repos.VerificationTokenRepository;
import com.nadia.users.service.exceptions.EmailAlreadyExistsException;
import com.nadia.users.service.exceptions.ExpiredTokenException;
import com.nadia.users.service.exceptions.InvalidTokenException;
import com.nadia.users.service.register.RegistrationRequest;
import com.nadia.users.util.EmailSender;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRep;
    
    @Autowired
    RoleRepository roleRep;
    
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    VerificationTokenRepository verificationTokenRepo;
    
    @Autowired
    EmailSender emailSender;

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRep.save(user);
    }

    @Override
    public User addRoleToUser(String username, String rolename) {
        User usr = userRep.findByUsername(username);
        Role r = roleRep.findByRole(rolename);
        usr.getRoles().add(r);
        return usr;
    }

    @Override
    public Role addRole(Role role) {
    	return roleRep.save(role); 
    }

    @Override
    public User findUserByUsername(String username) {
    	return userRep.findByUsername(username);
    }

    @Override
    public List<User> findAllUsers() { 
    	return userRep.findAll(); 
    }

    
    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> existing = userRep.findByEmail(request.getEmail());
        if (existing.isPresent())
            throw new EmailAlreadyExistsException("Email déjà existant !");

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        newUser.setEnabled(false);         
        userRep.save(newUser);

        //ajouter à newUser le role par défaut USER
        Role r = roleRep.findByRole("USER");
        List<Role> roles = new ArrayList<>();
        roles.add(r);
        newUser.setRoles(roles);
        userRep.save(newUser);

        //génére le code secret
        String code = generateCode();
        VerificationToken token = new VerificationToken(code, newUser);
        verificationTokenRepo.save(token);

        sendVerificationEmail(newUser, code);

        return newUser;
    }

    
    public String generateCode() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    
    public void sendVerificationEmail(User user, String code) {
        String body = "Bonjour <h1>" + user.getUsername() + "</h1>"
                    + "Votre code de validation est : <h1>" + code + "</h1>";
        emailSender.sendEmail(user.getEmail(), body);
    }

    
    @Override
    public User validateToken(String code) {
        VerificationToken token = verificationTokenRepo.findByToken(code);

        if (token == null)
            throw new InvalidTokenException("Code invalide !");

      
        Calendar now = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - now.getTime().getTime()) <= 0) {
            verificationTokenRepo.delete(token);
            throw new ExpiredTokenException("Code expiré !");
        }

        User user = token.getUser();
        user.setEnabled(true);
        userRep.save(user);
        verificationTokenRepo.delete(token);   
        return user;
    }
}