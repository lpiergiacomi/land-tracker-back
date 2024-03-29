package com.api.landtracker.repository;

import com.api.landtracker.model.entities.Payment;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"client", "user", "file"})
    Optional<Payment> findById(@NonNull Long id);

    @EntityGraph(attributePaths = {"client", "user", "file"})
    List<Payment> findAllByLotId(Long id);
}
