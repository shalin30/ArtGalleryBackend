package org.example.repository;

import org.example.entity.BlacklistedToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends CrudRepository<BlacklistedToken, String> {
    boolean existsByToken(String token);
}
