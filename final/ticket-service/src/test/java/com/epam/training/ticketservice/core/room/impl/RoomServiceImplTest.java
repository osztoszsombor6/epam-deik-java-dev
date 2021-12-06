/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author zsombor2
 */
public class RoomServiceImplTest {
    
    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private RoomService underTest = new RoomServiceImpl(roomRepository);

    
    @Test
    public void testGetRoomList() {
        RoomDto expected = new RoomDto("room1", 10, 15);
        List<RoomDto> expectedList = new ArrayList<>();
        expectedList.add(expected);
        Room expectedEntity = new Room("room1", 10, 15);
        List<Room> expectedEntityList = new ArrayList<>();
        expectedEntityList.add(expectedEntity);
        when(roomRepository.findAll()).thenReturn(expectedEntityList);
        List<RoomDto> result = underTest.getRoomList();
        assertEquals(expectedList, result);
    }

    @Test
    public void testDeleteRoom() {
        Room room = new Room("room1", 10, 15);
        when(roomRepository.findByName("room1")).thenReturn(Optional.of(room));
        
        underTest.deleteRoom("room1");
        verify(roomRepository).delete(room);
    }
    
    @Test
    public void testUpdateRoom() {
        Room room = new Room("room1", 10, 15);
        RoomDto roomDto = new RoomDto("room1", 15, 20);
        when(roomRepository.findByName("room1")).thenReturn(Optional.of(room));
        
        Room resultRoom = new Room("room1", 15, 20);
        
        underTest.updateRoom(roomDto);
        verify(roomRepository).save(resultRoom);
    }
    
    @Test
    public void testCreateMovie() {
        RoomDto roomDto = new RoomDto("room1", 15, 20);
        
        Room resultRoom = new Room("room1", 15, 20);
        
        underTest.createRoom(roomDto);
        verify(roomRepository).save(resultRoom);
    }


    @Test
    public void testConvertEntityToDto() {
        Optional<Room> room = Optional.of(new Room("room1", 15, 20));
        Optional<RoomDto> expected = Optional.of(new RoomDto("room1", 15, 20));
        when(roomRepository.findByName("room1")).thenReturn(room);
        
        Optional<RoomDto> actual = underTest.getRoomByName("room1");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConvertEntityToDtoIfMovieIsNotFound() {
        Optional<RoomDto> expected = Optional.empty();
        when(roomRepository.findByName("room1")).thenReturn(Optional.empty());
        
        Optional<RoomDto> actual = underTest.getRoomByName("room1");
        
        assertEquals(expected, actual);
    }
    
}
