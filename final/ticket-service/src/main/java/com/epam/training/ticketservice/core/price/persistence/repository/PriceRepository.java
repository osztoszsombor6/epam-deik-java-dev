package com.epam.training.ticketservice.core.price.persistence.repository;


import com.epam.training.ticketservice.core.price.persistence.entity.PriceComponent;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<PriceComponent, Integer> {

    Optional<PriceComponent> findByName(String name);
    
    Optional<PriceComponent> findByNameIsNull();
}
