package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.price.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.price.persistence.repository.PriceRepository;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    
    @Autowired
    private PriceRepository priceRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    
    @Override
    public List<RoomDto> getRoomList() {
        return roomRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<RoomDto> getRoomByName(String roomName) {
        return convertEntityToDto(roomRepository.findByName(roomName));
    }

    @Override
    public void createRoom(RoomDto roomDto) {
        Objects.requireNonNull(roomDto, "Room cannot be null");
        Objects.requireNonNull(roomDto.getName(), "Room Name cannot be null");
        Objects.requireNonNull(roomDto.getRowNum(), "Room number of rows cannot be null");
        Objects.requireNonNull(roomDto.getColNum(), "Room number of columns cannot be null");
        Room room = new Room(roomDto.getName(),
            roomDto.getRowNum(),
            roomDto.getColNum());
        roomRepository.save(room);
    }
    
    @Override
    public void updateRoom(RoomDto roomDto) {
        Optional<Room> room = roomRepository.findByName(roomDto.getName());
        room.ifPresent(r -> {
            r.setRowNum(roomDto.getRowNum());
            r.setColNum(roomDto.getColNum());
            roomRepository.save(r);
        });
    }

    @Override
    public void deleteRoom(String name) {
        Optional<Room> room = roomRepository.findByName(name);
        room.ifPresent(r -> {
            roomRepository.delete(r);
        });
    }


    public RoomDto convertEntityToDto(Room room) {
        return RoomDto.builder()
            .withName(room.getName())
            .withRowNum(room.getRowNum())
            .withColNum(room.getColNum())
            .build();
    }

    private Optional<RoomDto> convertEntityToDto(Optional<Room> room) {
        return room.isEmpty() ? Optional.empty() : Optional.of(convertEntityToDto(room.get()));
    }


}
