/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.core.price.impl;

import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.price.persistence.entity.MoviePriceComponent;
import com.epam.training.ticketservice.core.price.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.price.persistence.entity.RoomPriceComponent;
import com.epam.training.ticketservice.core.price.persistence.entity.ScreeningPriceComponent;
import com.epam.training.ticketservice.core.price.persistence.repository.MoviePriceComponentRepository;
import com.epam.training.ticketservice.core.price.persistence.repository.PriceRepository;
import com.epam.training.ticketservice.core.price.persistence.repository.RoomPriceComponentRepository;
import com.epam.training.ticketservice.core.price.persistence.repository.ScreeningPriceComponentRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 *
 * @author zsombor2
 */
public class PriceServiceImplTest {
    
    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final ScreeningRepository screeningRepository = mock(ScreeningRepository.class);
    private final PriceRepository priceRepository = mock(PriceRepository.class);
    private final RoomPriceComponentRepository roomPriceComponentRepository = 
                                                        mock(RoomPriceComponentRepository.class);
    private final MoviePriceComponentRepository moviePriceComponentRepository = 
                                                        mock(MoviePriceComponentRepository.class);
    private final ScreeningPriceComponentRepository screeningPriceComponentRepository = 
                                                        mock(ScreeningPriceComponentRepository.class);
    
    
    PriceServiceImpl underTest = new PriceServiceImpl(priceRepository, 
            roomRepository, movieRepository, screeningRepository, 
            roomPriceComponentRepository, moviePriceComponentRepository, screeningPriceComponentRepository);
    
    /**
     * Test of getBasePrice method, of class PriceServiceImpl.
     */
    @Test
    public void testGetBasePrice() {
        PriceComponent base = new PriceComponent(null, 1500);
        when(priceRepository.findByNameIsNull()).thenReturn(Optional.of(base));
        Integer expected = 1500;
        
        Integer actual = underTest.getBasePrice();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetBasePriceNotStored() {
        when(priceRepository.findByNameIsNull()).thenReturn(Optional.empty());
        Integer expected = 1500;
        
        Integer actual = underTest.getBasePrice();
        
        assertEquals(expected, actual);
    }

    /**
     * Test of updateBasePrice method, of class PriceServiceImpl.
     */
    @Test
    public void testUpdateBasePrice() {
        PriceComponent base = new PriceComponent(null, 1500);
        when(priceRepository.findByNameIsNull()).thenReturn(Optional.of(base));
        PriceComponent expected = new PriceComponent(null, 2000);
        
        underTest.updateBasePrice(2000);
        
        verify(priceRepository).save(expected);
    }
    
    @Test
    public void testUpdateBasePriceNotStored() {
        when(priceRepository.findByNameIsNull()).thenReturn(Optional.empty());
        PriceComponent expected = new PriceComponent(null, 2000);
        
        underTest.updateBasePrice(2000);
        
        verify(priceRepository).save(expected);
    }

    /**
     * Test of getPrice method, of class PriceServiceImpl.
     */
    @Test
    public void testGetPrice() {
        Room room = new Room("room1", 10, 10);
        Movie movie = new Movie("movie1", "drama", 100);
        Screening screening = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        
        Seat seat1 = new Seat(null,1,1);
        Seat seat2 = new Seat(null,1,2);
        List<Seat> seatList = new ArrayList<>();
        seatList.add(seat1);
        seatList.add(seat2);
        
        PriceComponent pc = new PriceComponent("pc", 500);
        
        RoomPriceComponent rpc = new RoomPriceComponent();
        rpc.setPriceComponent(pc);
        List<RoomPriceComponent> rpcList = new ArrayList<>();
        rpcList.add(rpc);
        MoviePriceComponent mpc = new MoviePriceComponent();
        mpc.setPriceComponent(pc);
        List<MoviePriceComponent> mpcList = new ArrayList<>();
        mpcList.add(mpc);
        ScreeningPriceComponent spc = new ScreeningPriceComponent();
        spc.setPriceComponent(pc);
        List<ScreeningPriceComponent> spcList = new ArrayList<>();
        spcList.add(spc);
        
        when(roomPriceComponentRepository.findByRoom(room)).thenReturn(rpcList);
        when(moviePriceComponentRepository.findByMovie(movie)).thenReturn(mpcList);
        when(screeningPriceComponentRepository.findByScreening(screening)).thenReturn(spcList);
        
        Integer expected = 6000;
        
        Integer actual = underTest.getPrice(screening, seatList);
        
        assertEquals(expected, actual);
    }

    /**
     * Test of createPriceComponent method, of class PriceServiceImpl.
     */
    @Test
    public void testCreatePriceComponentWhenPresent() {
        PriceComponent pc = new PriceComponent("pc", 1500);
        when(priceRepository.findByName("pc")).thenReturn(Optional.of(pc));
        PriceComponent expected = new PriceComponent("pc", 500);
        
        underTest.createPriceComponent("pc", 500);
        
        verify(priceRepository).save(expected);
    }
    
    @Test
    public void testCreatePriceComponent() {
        when(priceRepository.findByName("pc")).thenReturn(Optional.empty());
        PriceComponent expected = new PriceComponent("pc", 500);
        
        underTest.createPriceComponent("pc", 500);
        
        verify(priceRepository).save(expected);
    }

    /**
     * Test of attachPriceComponentToRoom method, of class PriceServiceImpl.
     */
    @Test
    public void testAttachPriceComponentToRoom() {
        PriceComponent pc = new PriceComponent("pc", 500);
        Room room = new Room("room1", 10, 10);
        RoomPriceComponent rpc = new RoomPriceComponent();
        rpc.setPriceComponent(pc);
        rpc.setRoom(room);
        
        when(roomRepository.findByName("room1")).thenReturn(Optional.of(room));
        when(priceRepository.findByName("pc")).thenReturn(Optional.of(pc));
        
        underTest.attachPriceComponentToRoom("pc", "room1");
        
        verify(roomPriceComponentRepository).save(rpc);
    }
    
    @Test
    public void testAttachPriceComponentToRoomWhenRoomIsInvalid() {
        PriceComponent pc = new PriceComponent("pc", 500);
        
        when(roomRepository.findByName("room1")).thenReturn(Optional.empty());
        when(priceRepository.findByName("pc")).thenReturn(Optional.of(pc));
        
        underTest.attachPriceComponentToRoom("pc", "room1");
        
        verifyNoMoreInteractions(roomPriceComponentRepository);
    }
    
    @Test
    public void testAttachPriceComponentToRoomWhenPriceComponentIsInvalid() {
        Room room = new Room("room1", 10, 10);
        
        when(roomRepository.findByName("room1")).thenReturn(Optional.of(room));
        when(priceRepository.findByName("pc")).thenReturn(Optional.empty());
        
        underTest.attachPriceComponentToRoom("pc", "room1");
        
        verifyNoMoreInteractions(roomPriceComponentRepository);
    }
    
    @Test
    public void testAttachPriceComponentToMovie() {
        PriceComponent pc = new PriceComponent("pc", 500);
        Movie movie = new Movie("movie1", "drama", 100);
        MoviePriceComponent mpc = new MoviePriceComponent();
        mpc.setPriceComponent(pc);
        mpc.setMovie(movie);
        
        when(movieRepository.findByTitle("movie1")).thenReturn(Optional.of(movie));
        when(priceRepository.findByName("pc")).thenReturn(Optional.of(pc));
        
        underTest.attachPriceComponentToMovie("pc", "movie1");
        
        verify(moviePriceComponentRepository).save(mpc);
    }
    
    @Test
    public void testAttachPriceComponentToMovieWhenMovieIsInvalid() {
        PriceComponent pc = new PriceComponent("pc", 500);
        
        when(movieRepository.findByTitle("movie1")).thenReturn(Optional.empty());
        when(priceRepository.findByName("pc")).thenReturn(Optional.of(pc));
        
        underTest.attachPriceComponentToMovie("pc", "movie1");
        
        verifyNoMoreInteractions(moviePriceComponentRepository);
    }
    
    @Test
    public void testAttachPriceComponentToMovieWhenPriceComponentIsInvalid() {
        Movie movie = new Movie("movie1", "drama", 100);
        
        when(movieRepository.findByTitle("movie1")).thenReturn(Optional.of(movie));
        when(priceRepository.findByName("pc")).thenReturn(Optional.empty());
        
        underTest.attachPriceComponentToMovie("pc", "movie1");
        
        verifyNoMoreInteractions(moviePriceComponentRepository);
    }
    
    @Test
    public void testAttachPriceComponentToScreening() {
        PriceComponent pc = new PriceComponent("pc", 500);
        Movie movie = new Movie("movie1", "drama", 100);
        Room room = new Room("room1", 10, 10);
        Screening screening = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        List<Screening> screeningList = new ArrayList<>();
        screeningList.add(screening);
        ScreeningPriceComponent spc = new ScreeningPriceComponent();
        spc.setPriceComponent(pc);
        spc.setScreening(screening);
        
        when(screeningRepository.findByMovie_TitleAndRoom_NameAndStartDate("movie1", "room1", 
                LocalDateTime.of(2000, Month.MARCH, 1, 12, 0)))
                .thenReturn(screeningList);
        when(priceRepository.findByName("pc")).thenReturn(Optional.of(pc));
        
        underTest.attachPriceComponentToScreening("pc","movie1", "room1", 
                                        LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        
        verify(screeningPriceComponentRepository).save(spc);
    }
    
    @Test
    public void testAttachPriceComponentToScreeningWhenScreeningIsInvalid() {
        PriceComponent pc = new PriceComponent("pc", 500);
        when(screeningRepository.findByMovie_TitleAndRoom_NameAndStartDate("movie1", "room1", 
                LocalDateTime.of(2000, Month.MARCH, 1, 12, 0)))
                .thenReturn(Collections.EMPTY_LIST);
        when(priceRepository.findByName("pc")).thenReturn(Optional.of(pc));
        
        underTest.attachPriceComponentToScreening("pc","movie1", "room1", 
                                        LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        
        verifyNoMoreInteractions(screeningPriceComponentRepository);
    }
    
    @Test
    public void testAttachPriceComponentToScreeningWhenPriceComponentIsInvalid() {
        PriceComponent pc = new PriceComponent("pc", 500);
        Movie movie = new Movie("movie1", "drama", 100);
        Room room = new Room("room1", 10, 10);
        Screening screening = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        List<Screening> screeningList = new ArrayList<>();
        screeningList.add(screening);
        ScreeningPriceComponent spc = new ScreeningPriceComponent();
        spc.setPriceComponent(pc);
        spc.setScreening(screening);
        
        when(screeningRepository.findByMovie_TitleAndRoom_NameAndStartDate("movie1", "room1", 
                LocalDateTime.of(2000, Month.MARCH, 1, 12, 0)))
                .thenReturn(screeningList);
        when(priceRepository.findByName("pc")).thenReturn(Optional.empty());
        
        underTest.attachPriceComponentToScreening("pc","movie1", "room1", 
                                        LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        
        verifyNoMoreInteractions(screeningPriceComponentRepository);
    }
    
}
