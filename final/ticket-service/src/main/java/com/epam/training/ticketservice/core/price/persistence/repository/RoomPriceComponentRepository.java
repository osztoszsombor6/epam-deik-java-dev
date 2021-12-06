package com.epam.training.ticketservice.core.price.persistence.repository;


import com.epam.training.ticketservice.core.price.persistence.entity.RoomPriceComponent;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomPriceComponentRepository extends JpaRepository<RoomPriceComponent, Integer> {
    
    List<RoomPriceComponent> findByRoom(Room room);
    
}