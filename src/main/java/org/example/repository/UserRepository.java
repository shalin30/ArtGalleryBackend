package org.example.repository;

import org.example.entity.UserDetails;
import org.example.json.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDetails, Integer> {
    UserDetails findByUserId(Integer userId);
    UserDetails findByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    UserDetails findByEmail(String email);
}
