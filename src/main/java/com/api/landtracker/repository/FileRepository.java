package com.api.landtracker.repository;

import com.api.landtracker.model.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, String> {
}
