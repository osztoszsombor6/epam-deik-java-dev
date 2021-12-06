/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.converter.LocalDateTime2StringConverter;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author zsombor2
 */
public class UserCommandTest {
    
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = mock(UserService.class);
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final LocalDateTime2StringConverter converter = new LocalDateTime2StringConverter();
    
    private UserCommand underTest = new UserCommand(userService, bookingRepository, converter);

    
    @Test
    public void testPrintUserInformationIfUserNotLoggedIn() {
        when(userService.getLoggedInUser()).thenReturn(Optional.empty());
        String expected = "You are not signed in";
        String actual = underTest.printUserInformation();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testPrintUserInformationIfUserIsAdmin() {
        UserDto userDto = new UserDto("user", User.Role.ADMIN);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        
        String expected = "Signed in with privileged account 'user'";
        String actual = underTest.printUserInformation();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testPrintUserInformationWithoutBooking() {
        UserDto userDto = new UserDto("user", User.Role.USER);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        when(bookingRepository.findByUser_Username("user")).thenReturn(Collections.EMPTY_LIST);
        
        String expected = "Signed in with account 'user'\nYou have not booked any tickets yet";
        String actual = underTest.printUserInformation();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testPrintUserInformationWithBooking() {
        UserDto userDto = new UserDto("user", User.Role.USER);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        List<Booking> bookingList = new ArrayList<>();
        Room room = new Room("room1", 10, 10);
        Movie movie = new Movie("movie1", "drama", 100);
        Screening screening = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        User user = new User("user", "password", User.Role.USER);
        List<Seat> seatList = new ArrayList<>();
        seatList.add(new Seat(null, 5, 5));
        seatList.add(new Seat(null, 5, 6));
        Booking booking = new Booking(user, screening, seatList, 3000);
        bookingList.add(booking);
        when(bookingRepository.findByUser_Username("user")).thenReturn(bookingList);
        
        String expected = "Signed in with account 'user'\nYour previous bookings are\n"
                + "Seats (5,5), (5,6) on movie1 in room room1 starting at 2000-03-01 12:00 for 3000 HUF";
        String actual = underTest.printUserInformation();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLoginPrivilegedAsAdmin() {
        User user = new User("adming", "admin", User.Role.ADMIN);
        UserDto userDto = new UserDto("admin", User.Role.ADMIN);
        when(userService.getUserRole("admin")).thenReturn(User.Role.ADMIN);
        when(userService.login("admin", "admin")).thenReturn(Optional.of(userDto));
        
        String expected = "UserDto{username='admin', role=ADMIN} is logged in!";
        String actual = underTest.loginPrivileged("admin", "admin");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLoginPrivilegedAsUser() {
        User user = new User("user", "user", User.Role.USER);
        UserDto userDto = new UserDto("user", User.Role.USER);
        when(userService.getUserRole("user")).thenReturn(User.Role.USER);
        when(userService.login("user", "user")).thenReturn(Optional.of(userDto));
        
        String expected = "You are not an admin!";
        String actual = underTest.loginPrivileged("user", "user");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLoginPrivilegedIncorrectCred() {
        User user = new User("user", "user", User.Role.ADMIN);
        UserDto userDto = new UserDto("user", User.Role.ADMIN);
        when(userService.getUserRole("user")).thenReturn(User.Role.ADMIN);
        when(userService.login("user", "asd")).thenReturn(Optional.empty());
        
        String expected = "Login failed due to incorrect credentials";
        String actual = underTest.loginPrivileged("user", "asd");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLogout() {
        UserDto userDto = new UserDto("user", User.Role.USER);
        when(userService.logout()).thenReturn(Optional.of(userDto));
        
        String expected = "UserDto{username='user', role=USER} is logged out!";
        String actual = underTest.logout();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLogoutWhenNotLoggedIn() {
        when(userService.logout()).thenReturn(Optional.empty());
        
        String expected = "You need to login first!";
        String actual = underTest.logout();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLogin() {
        UserDto userDto = new UserDto("user", User.Role.USER);
        when(userService.login("user", "user")).thenReturn(Optional.of(userDto));
        
        String expected = "UserDto{username='user', role=USER} is logged in!";
        String actual = underTest.login("user", "user");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLoginWithWrongCredentials() {
        when(userService.login("user", "user")).thenReturn(Optional.empty());
        
        String expected = "Login failed due to incorrect credentials";
        String actual = underTest.login("user", "user");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRegisterUser() {        
        
        String expected = "Registration was successful!";
        
        String actual = underTest.registerUser("user", "user");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRegisterUserWithTakenUserName() {        
        
        String expected = "Registration failed!";
        doThrow(NullPointerException.class)
                .when(userService)
                .registerUser("user", "user");
        
        String actual = underTest.registerUser("user", "user");
        
        assertEquals(expected, actual);
    }
}
