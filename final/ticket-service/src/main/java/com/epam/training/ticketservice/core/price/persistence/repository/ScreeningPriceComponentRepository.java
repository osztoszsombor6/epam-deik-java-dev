package com.epam.training.ticketservice.core.price.persistence.repository;


import com.epam.training.ticketservice.core.price.persistence.entity.ScreeningPriceComponent;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreeningPriceComponentRepository extends JpaRepository<ScreeningPriceComponent, Integer> {
    
    List<ScreeningPriceComponent> findByScreening(Screening screening);
    
}
