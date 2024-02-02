package com.nexola.dscatalog.repositories;

import com.nexola.dscatalog.entities.PasswordRecover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {
    
    @Query("SELECT obj FROM PasswordRecover obj WHERE obj.token = :token AND obj.expiration > :now")
    List<PasswordRecover> searchValidTokens(String token, Instant now);

}
