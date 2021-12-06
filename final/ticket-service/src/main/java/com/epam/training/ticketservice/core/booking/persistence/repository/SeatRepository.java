package com.epam.training.ticketservice.core.booking.persistence.repository;

import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer>  {
    List<Seat> findByBooking_Screening_Id(Integer id);
}
