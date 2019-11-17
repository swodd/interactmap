package com.swodd.interactmap.repositories;

import com.swodd.interactmap.entities.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentsRepository extends JpaRepository<Apartment, Integer> {
    Page<Apartment> findAll(Pageable pageable);
}
