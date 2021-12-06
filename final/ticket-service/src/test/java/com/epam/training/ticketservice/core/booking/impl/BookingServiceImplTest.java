/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.model.SeatAlreadyBookedException;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.booking.persistence.repository.SeatRepository;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.price.PriceService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
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

/**
 *
 * @author zsombor2
 */
public class BookingServiceImplTest {
    
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = mock(UserService.class);
    private final ScreeningRepository screeningRepository = mock(ScreeningRepository.class);
    private final ScreeningService screeningService = mock(ScreeningServiceImpl.class);
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final SeatRepository seatRepository = mock(SeatRepository.class);
    private final PriceService priceService = mock(PriceService.class);
    
    private BookingServiceImpl underTest = new BookingServiceImpl(screeningRepository, userRepository, userService,
                                                                bookingRepository, screeningService, seatRepository,
                                                                priceService);

    /**
     * Test of getSeatList method, of class BookingServiceImpl.
     */
    @Test
    public void testGetSeatList() {
        String seats = "1,1 1,2";
        Seat seat1 = new Seat(null,1,1);
        Seat seat2 = new Seat(null,1,2);
        List<Seat> expected = new ArrayList<>();
        expected.add(seat1);
        expected.add(seat2);
        
        List<Seat> actual = underTest.getSeatList(seats);
        
        assertEquals(expected, actual);
    }

    /**
     * Test of createBooking method, of class BookingServiceImpl.
     */
    @Test
    public void testCreateBooking() throws Exception {
        UserDto userDto = new UserDto("user", User.Role.USER);
        User user = new User("user","password", User.Role.USER);
        Room room = new Room("room1", 10, 10);
        RoomDto roomDto = new RoomDto(room.getName(), room.getRowNum(), room.getColNum());
        Movie movie = new Movie("movie1", "drama", 100);
        MovieDto movieDto = new MovieDto("movie1", "drama", 100);
        Screening screening = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        ScreeningDto screeningDto = new ScreeningDto(movieDto, roomDto, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
//        screening.setId(1);
        List<Screening> screeningList = new ArrayList<>();
        screeningList.add(screening);
        
        String seats = "1,1 1,2";
        Seat seat1 = new Seat(null,1,1);
        Seat seat2 = new Seat(null,1,2);
        List<Seat> seatList = new ArrayList<>();
        seatList.add(seat1);
        seatList.add(seat2);

        
        Booking expected = new Booking(user, screening, seatList, 3000);
        System.out.println("expected:" + expected);
        
        when(screeningRepository.findByMovie_TitleAndRoom_NameAndStartDate("movie1", "room1", 
                                            LocalDateTime.of(2000, Month.MARCH, 1, 12, 0))).thenReturn(screeningList);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(priceService.getPrice(screening, seatList)).thenReturn(3000);
        when(seatRepository.findByBooking_Screening_Id(1)).thenReturn(Collections.EMPTY_LIST);
        when(bookingRepository.save(expected)).thenReturn(expected);
        when(screeningService.convertEntityToDto(screening)).thenReturn(screeningDto);
        
        underTest.createBooking("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0), seats);
        
        verify(bookingRepository).save(expected);
    }

    /**
     * Test of showPrice method, of class BookingServiceImpl.
     */
    @Test
    public void testShowPriceWithInvalidScreening() throws Exception {
        when(screeningRepository.findByMovie_TitleAndRoom_NameAndStartDate("movie1", "room1", 
                LocalDateTime.of(2000, Month.MARCH, 1, 12, 0))).thenReturn(Collections.EMPTY_LIST);
        
        Integer expected = 0;
        
        Integer actual = underTest.showPrice("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0), "1,1");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testShowPriceWithValidScreening() throws Exception {
        Movie movie = new Movie("movie1", "drama", 100);
        Room room = new Room("room1", 10, 10);
        Screening screening = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        List<Screening> screeningList = new ArrayList<>();
        screeningList.add(screening);
        Seat seat1 = new Seat(null, 1, 1);
        List<Seat> seatList = new ArrayList<>();
        seatList.add(seat1);
        when(screeningRepository.findByMovie_TitleAndRoom_NameAndStartDate("movie1", "room1", 
                LocalDateTime.of(2000, Month.MARCH, 1, 12, 0))).thenReturn(screeningList);
        when(priceService.getPrice(screening, seatList)).thenReturn(1500);
        
        Integer expected = 1500;
        
        Integer actual = underTest.showPrice("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0), "1,1");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCheckBookingIsPossible() throws NoSuchMethodException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException {
        User user = new User("user","password", User.Role.USER);
        Room room = new Room("room1", 10, 10);
        Movie movie = new Movie("movie1", "drama", 100);
        Screening screening = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        screening.setId(1);
        
        Seat seat1 = new Seat(null,1,1);
        List<Seat> seatList = new ArrayList<>();
        seatList.add(seat1);

        Booking booking = new Booking(user, screening, seatList, 1500);
        
        when(seatRepository.findByBooking_Screening_Id(1)).thenReturn(seatList);
        
        Method checkBookingIsPossible = 
                BookingServiceImpl.class.getDeclaredMethod("checkBookingIsPossible", Booking.class);
        checkBookingIsPossible.setAccessible(true);
        try {
            checkBookingIsPossible.invoke(underTest, booking);
            assertTrue(false);
        } catch(Exception ex) {
            assertEquals(ex.getCause().getClass(), SeatAlreadyBookedException.class);
        }
                
    }
    
}
