/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.model.InvalidSeatException;
import com.epam.training.ticketservice.core.booking.model.SeatAlreadyBookedException;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.shell.Availability;

/**
 *
 * @author zsombor2
 */
public class BookingCommandTest {
    
    private final BookingService bookingService = mock(BookingService.class);
    private final UserService userService = mock(UserService.class);
    
    private BookingCommand underTest = new BookingCommand(bookingService, userService);
    
    /**
     * Test of createBooking method, of class BookingCommand.
     */
    @Test
    public void testCreateBookingInvalidSeat() throws InvalidSeatException, SeatAlreadyBookedException {
        when(bookingService.createBooking("movie1", "room1", LocalDateTime.MAX, "15,50"))
                .thenThrow(new InvalidSeatException(15,50));
        
        String expected = "Seat (15,50) does not exist in this room";
           
        String actual = underTest.createBooking("movie1", "room1", LocalDateTime.MAX, "15,50");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateBookingBookedSeat() throws InvalidSeatException, SeatAlreadyBookedException {
        when(bookingService.createBooking("movie1", "room1", LocalDateTime.MAX, "15,50"))
                .thenThrow(new SeatAlreadyBookedException(15,50));
        
        String expected = "Seat (15,50) is already taken";
           
        String actual = underTest.createBooking("movie1", "room1", LocalDateTime.MAX, "15,50");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateBooking() throws InvalidSeatException, SeatAlreadyBookedException {
        UserDto userDto = new UserDto("user", User.Role.USER);
        RoomDto roomDto = new RoomDto("room1", 10, 10);
        MovieDto movieDto = new MovieDto("movie1", "drama", 100);
        ScreeningDto screeningDto = new ScreeningDto(movieDto, roomDto, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        
        SeatDto seat1 = new SeatDto(1,1);
        SeatDto seat2 = new SeatDto(1,2);
        List<SeatDto> seatList = new ArrayList<>();
        seatList.add(seat1);
        seatList.add(seat2);

        BookingDto expectedBookingDto = new BookingDto(userDto, screeningDto, seatList, 3000);
        
        when(bookingService.createBooking("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0), "1,1 1,2"))
                .thenReturn(expectedBookingDto);
        String expected = "Seats booked: (1,1), (1,2); the price for this booking is 3000 HUF";
           
        String actual = underTest.createBooking("movie1", "room1", 
                            LocalDateTime.of(2000, Month.MARCH, 1, 12, 0), "1,1 1,2");
        
        assertEquals(expected, actual);
    }

    /**
     * Test of showPrice method, of class BookingCommand.
     */
    @Test
    public void testShowPriceInvalidSeat() throws InvalidSeatException, SeatAlreadyBookedException {
        when(bookingService.showPrice("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0), "15,50"))
                .thenThrow(new InvalidSeatException(15,50));
        
        String expected = "Seat (15,50) does not exist in this room";
           
        String actual = underTest.showPrice("movie1", "room1", 
                                LocalDateTime.of(2000, Month.MARCH, 1, 12, 0), "15,50");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testShowPriceBookedSeat() throws InvalidSeatException, SeatAlreadyBookedException {
        when(bookingService.showPrice("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0), "15,50"))
                .thenThrow(new SeatAlreadyBookedException(15,50));
        
        String expected = "Seat (15,50) is already taken";
           
        String actual = underTest.showPrice("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0), "15,50");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testShowPrice() throws InvalidSeatException, SeatAlreadyBookedException {
        when(bookingService.showPrice("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0), "15,50"))
                .thenReturn(1500);
        
        String expected = "The price for this booking would be 1500 HUF";
           
        String actual = underTest.showPrice("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0), "15,50");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsAvailableUser() throws NoSuchMethodException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException {
        UserDto userDto = new UserDto("user", User.Role.USER);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        Availability expected = Availability.available();
        
        Method isAvailable = BookingCommand.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);
        
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }
    
    @Test
    public void testIsAvailableAdmin() throws NoSuchMethodException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException {
        UserDto userDto = new UserDto("user", User.Role.ADMIN);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        Availability expected = Availability.unavailable("Only logged in non-admin users can book");
        
        Method isAvailable = BookingCommand.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);
        
        assertEquals(expected.isAvailable(), actual.isAvailable());
        assertEquals(expected.getReason(), actual.getReason());
    }
    
    @Test
    public void testIsAvailableNotLoggedIn() throws NoSuchMethodException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException {
        when(userService.getLoggedInUser()).thenReturn(Optional.empty());
        Availability expected = Availability.unavailable("Only logged in non-admin users can book");
        
        Method isAvailable = BookingCommand.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);
        
        assertEquals(expected.isAvailable(), actual.isAvailable());
        assertEquals(expected.getReason(), actual.getReason());
    }
}
