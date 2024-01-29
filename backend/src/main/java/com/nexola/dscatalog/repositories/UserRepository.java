package com.nexola.dscatalog.repositories;

import com.nexola.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
