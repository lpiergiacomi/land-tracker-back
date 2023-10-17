package com.api.landtracker.repository;

import com.api.landtracker.model.dto.IUserWithAssignedLots;
import com.api.landtracker.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT user.id, user.username, GROUP_CONCAT(lot.id SEPARATOR ', ') as assignedLotsIdsString " +
            " FROM User user " +
            " LEFT JOIN lot_assignment lot_assignment on lot_assignment.user_id = user.id " +
            " LEFT JOIN lot lot on lot.id = lot_assignment.lot_id " +
            " GROUP BY user.id ",
            nativeQuery = true)
    List<IUserWithAssignedLots> getAllUsersWithAssignedLots();
}
