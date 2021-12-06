package com.epam.training.ticketservice.core.booking.persistence.repository;


import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUser_Username(String name);
    
}
