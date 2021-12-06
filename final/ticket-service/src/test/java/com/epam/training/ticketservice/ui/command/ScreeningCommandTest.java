/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.converter.LocalDateTime2StringConverter;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.Month;
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
public class ScreeningCommandTest {
    
    private final ScreeningService screeningService = mock(ScreeningService.class);
    private final UserService userService = mock(UserService.class);
    private final LocalDateTime2StringConverter converter = new LocalDateTime2StringConverter();
    
    private ScreeningCommand underTest = new ScreeningCommand(converter, screeningService, userService);
    
    /**
     * Test of createScreening method, of class ScreeningCommand.
     */
    @Test
    public void testCreateScreening() {
        underTest.createScreening("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));

        verify(screeningService).createScreening("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
    }

    /**
     * Test of deleteScreening method, of class ScreeningCommand.
     */
    @Test
    public void testDeleteScreening() {
        underTest.deleteScreening("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));

        verify(screeningService).deleteScreening("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
    }

    /**
     * Test of listScreenings method, of class ScreeningCommand.
     */
    @Test
    public void testListScreeningsWithNoScreenings() {
        when(screeningService.getScreeningList()).thenReturn(Collections.EMPTY_LIST);
        String expected = "There are no screenings";
        
        String actual = underTest.listScreenings();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testListScreeningsWithScreenings() {
        RoomDto roomDto = new RoomDto("room1", 5, 5);
        MovieDto movieDto = new MovieDto("movie1", "drama", 100);
        ScreeningDto screening1 = new ScreeningDto(movieDto, roomDto, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        RoomDto roomDto2 = new RoomDto("room2", 5, 5);
        MovieDto movieDto2 = new MovieDto("movie2", "drama", 100);
        ScreeningDto screening2 = new ScreeningDto(movieDto2, roomDto2, LocalDateTime.of(2000, Month.MARCH, 1, 8, 0));
        List<ScreeningDto> screeningList = new ArrayList<>();
        screeningList.add(screening1);
        screeningList.add(screening2);
        when(screeningService.getScreeningList()).thenReturn(screeningList);
        String expected = "movie1 (drama, 100 minutes), screened in room room1, at 2000-03-01 12:00\n" +
            "movie2 (drama, 100 minutes), screened in room room2, at 2000-03-01 08:00";
        
        String actual = underTest.listScreenings();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsAvailableUser() throws NoSuchMethodException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException {
        UserDto userDto = new UserDto("user", User.Role.USER);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        Availability expected = Availability.unavailable("You are not an admin!");
        
        Method isAvailable = ScreeningCommand.class.getDeclaredMethod("isAvailable");
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
        
        Method isAvailable = ScreeningCommand.class.getDeclaredMethod("isAvailable");
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
        
        Method isAvailable = ScreeningCommand.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);
        
        assertEquals(expected.isAvailable(), actual.isAvailable());
        assertEquals(expected.getReason(), actual.getReason());
    }
    
}
