package com.medifly.repository;

import com.medifly.model.Salt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaltRepository extends JpaRepository<Salt, Long> {
    Optional<Salt> findBySaltNameIgnoreCase(String saltName);
}
