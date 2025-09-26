package com.esporte.myapp.repository;

import com.esporte.myapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);

    User findByid(String id);

    @Query("SELECT DISTINCT u FROM User u JOIN u.sports s WHERE u.id != :userId AND u.id NOT IN :excludedIds AND u.city = :city AND s IN :sports")
    List<User> findSuggestions(
            @Param("userId") String userId,
            @Param("city") String city,
            @Param("sports") List<String> sports,
            @Param("excludedIds") Set<String> excludedIds
    );
}