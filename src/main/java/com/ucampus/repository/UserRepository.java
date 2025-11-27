// src/main/java/com/ucampus/repository/UserRepository.java
package com.ucampus.repository;

import com.ucampus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}