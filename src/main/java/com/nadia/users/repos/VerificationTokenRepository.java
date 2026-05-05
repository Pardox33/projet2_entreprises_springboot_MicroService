package com.nadia.users.repos;
 
import com.nadia.users.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}