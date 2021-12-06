package com.epam.training.ticketservice.core.screening.persistence.repository;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {
    List<Screening> findByMovie_TitleAndRoom_NameAndStartDate(String title, String name, LocalDateTime startDate);
    
    List<Screening> findByRoom_Name(String name);
}
