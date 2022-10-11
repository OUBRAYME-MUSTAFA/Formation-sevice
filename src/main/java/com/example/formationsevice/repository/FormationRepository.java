package com.example.formationsevice.repository;

import com.example.formationsevice.entities.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface FormationRepository extends JpaRepository<Formation, Long> {
    List<Formation> findAll();
}
