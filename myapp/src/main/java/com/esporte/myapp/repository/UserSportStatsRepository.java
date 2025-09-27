// com.esporte.myapp.repository.UserSportStatsRepository
package com.esporte.myapp.repository;

import com.esporte.myapp.entity.UserSportStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSportStatsRepository extends JpaRepository<UserSportStats, Long> {
    Optional<UserSportStats> findByUser_IdAndSport(String userId, String sport);
    List<UserSportStats> findByUser_Id(String userId);
}
