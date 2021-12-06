package com.epam.training.ticketservice.core.room;


import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import java.util.List;
import java.util.Optional;

public interface RoomService {

    List<RoomDto> getRoomList();

    Optional<RoomDto> getRoomByName(String roomName);

    void createRoom(RoomDto room);
    
    void updateRoom(RoomDto room);
    
    void deleteRoom(String name);
    
    public RoomDto convertEntityToDto(Room room);

}
