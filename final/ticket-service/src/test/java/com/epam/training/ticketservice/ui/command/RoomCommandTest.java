/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.shell.Availability;

/**
 *
 * @author zsombor2
 */
public class RoomCommandTest {
    
    private final RoomService roomService = mock(RoomService.class);
    private final UserService userService = mock(UserService.class);
    
    private RoomCommand underTest = new RoomCommand(roomService, userService);

    /**
     * Test of createRoom method, of class RoomCommand.
     */
    @Test
    public void testCreateRoom() {
        RoomDto roomDto = new RoomDto("room1", 5, 10);
        
        
        RoomDto actual = underTest.createRoom("room1", 5, 10);

        verify(roomService).createRoom(roomDto);
        assertEquals(roomDto, actual);
    }

    /**
     * Test of updateRoom method, of class RoomCommand.
     */
    @Test
    public void testUpdateRoom() {
        RoomDto roomDto = new RoomDto("room1", 5, 10);
        
        
        RoomDto actual = underTest.updateRoom("room1", 5, 10);

        verify(roomService).updateRoom(roomDto);
        assertEquals(roomDto, actual);
    }

    /**
     * Test of deleteRoom method, of class RoomCommand.
     */
    @Test
    public void testDeleteRoom() {
        underTest.deleteRoom("room1");

        verify(roomService).deleteRoom("room1");
    }

    /**
     * Test of listMovies method, of class RoomCommand.
     */
    @Test
    public void testListRoomsWithNoSavedRooms() {
        when(roomService.getRoomList()).thenReturn(Collections.EMPTY_LIST);
        String expected = "There are no rooms at the moment";
        
        String actual = underTest.listMovies();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testListRoomsWithSavedRoom() {
        RoomDto room1 = new RoomDto("room1", 10, 15);
        List<RoomDto> roomList = new ArrayList<>();
        roomList.add(room1);
        when(roomService.getRoomList()).thenReturn(roomList);
        String expected = "Room room1 with 150 seats, 10 rows and 15 columns";
        
        String actual = underTest.listMovies();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testListRoomsWithSavedRooms() {
        RoomDto room1 = new RoomDto("room1", 10, 15);
        RoomDto room2 = new RoomDto("room2", 10, 10);
        List<RoomDto> roomList = new ArrayList<>();
        roomList.add(room1);
        roomList.add(room2);
        when(roomService.getRoomList()).thenReturn(roomList);
        String expected = "Room room1 with 150 seats, 10 rows and 15 columns\n"
                + "Room room2 with 100 seats, 10 rows and 10 columns";
        
        String actual = underTest.listMovies();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsAvailableUser() throws NoSuchMethodException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException {
        UserDto userDto = new UserDto("user", User.Role.USER);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        Availability expected = Availability.unavailable("You are not an admin!");
        
        Method isAvailable = RoomCommand.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);
        
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }
    
    @Test
    public void testIsAvailableAdmin() throws NoSuchMethodException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException {
        UserDto userDto = new UserDto("user", User.Role.ADMIN);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        Availability expected = Availability.available();
        
        Method isAvailable = RoomCommand.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);
        
        assertEquals(expected.isAvailable(), actual.isAvailable());
        assertEquals(expected.getReason(), actual.getReason());
    }
    
    @Test
    public void testIsAvailableNotLoggedIn() throws NoSuchMethodException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException {
        when(userService.getLoggedInUser()).thenReturn(Optional.empty());
        Availability expected = Availability.unavailable("You are not an admin!");
        
        Method isAvailable = RoomCommand.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);
        
        assertEquals(expected.isAvailable(), actual.isAvailable());
        assertEquals(expected.getReason(), actual.getReason());
    }
}
