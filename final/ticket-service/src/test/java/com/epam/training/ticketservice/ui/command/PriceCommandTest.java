/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.price.PriceService;
import com.epam.training.ticketservice.core.price.persistence.repository.PriceRepository;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.shell.Availability;

/**
 *
 * @author zsombor2
 */
public class PriceCommandTest {
    
    private final RoomService roomService = mock(RoomService.class);
    private final UserService userService = mock(UserService.class);
    private final PriceService priceService = mock(PriceService.class);
    private final PriceRepository priceRepository = mock(PriceRepository.class);
    
    private PriceCommand underTest = new PriceCommand(userService, priceService, roomService, priceRepository);

    /**
     * Test of updateBasePrice method, of class PriceCommand.
     */
    @Test
    public void testUpdateBasePrice() {
        underTest.updateBasePrice(1000);
        verify(priceService).updateBasePrice(1000);
    }

    /**
     * Test of createPriceComponent method, of class PriceCommand.
     */
    @Test
    public void testCreatePriceComponent() {
        underTest.createPriceComponent("component1", 100);
        verify(priceService).createPriceComponent("component1", 100);
    }

    /**
     * Test of attachPriceComponentToRoom method, of class PriceCommand.
     */
    @Test
    public void testAttachPriceComponentToRoom() {
        underTest.attachPriceComponentToRoom("component1", "room1");
        verify(priceService).attachPriceComponentToRoom("component1", "room1");
    }

    /**
     * Test of attachPriceComponentToMovie method, of class PriceCommand.
     */
    @Test
    public void testAttachPriceComponentToMovie() {
        underTest.attachPriceComponentToMovie("component1", "movie1");
        verify(priceService).attachPriceComponentToMovie("component1", "movie1");
    }

    /**
     * Test of attachPriceComponentToScreening method, of class PriceCommand.
     */
    @Test
    public void testAttachPriceComponentToScreening() {
        underTest.attachPriceComponentToScreening("component1", "movie1", "room1",
                LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        verify(priceService).attachPriceComponentToScreening("component1", "movie1", "room1",
                LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
    }
    
    @Test
    public void testIsAvailableUser() throws NoSuchMethodException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException {
        UserDto userDto = new UserDto("user", User.Role.USER);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        Availability expected = Availability.unavailable("You are not an admin");
        
        Method isAvailable = PriceCommand.class.getDeclaredMethod("isAvailable");
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
        
        Method isAvailable = PriceCommand.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);
        
        assertEquals(expected.isAvailable(), actual.isAvailable());
        assertEquals(expected.getReason(), actual.getReason());
    }
    
    @Test
    public void testIsAvailableNotLoggedIn() throws NoSuchMethodException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException {
        when(userService.getLoggedInUser()).thenReturn(Optional.empty());
        Availability expected = Availability.unavailable("You are not an admin");
        
        Method isAvailable = PriceCommand.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);
        
        assertEquals(expected.isAvailable(), actual.isAvailable());
        assertEquals(expected.getReason(), actual.getReason());
    }
}
