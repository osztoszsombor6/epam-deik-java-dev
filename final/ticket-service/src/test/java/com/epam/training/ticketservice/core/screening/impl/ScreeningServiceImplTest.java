/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 *
 * @author zsombor2
 */
public class ScreeningServiceImplTest {
    
    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final MovieService movieService = mock(MovieService.class);
    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final RoomService roomService = mock(RoomService.class);
    private final ScreeningRepository screeningRepository = mock(ScreeningRepository.class);
    private ScreeningServiceImpl underTest = new ScreeningServiceImpl(roomRepository, roomService, movieRepository, movieService, screeningRepository);
    
    /**
     * Test of getScreeningList method, of class ScreeningServiceImpl.
     */
    @Test
    public void testGetScreeningList() {
        RoomDto roomDto = new RoomDto("room1", 10, 10);
        MovieDto movieDto = new MovieDto("movie1", "drama", 100);
        ScreeningDto screeningDto = new ScreeningDto(movieDto, roomDto, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        List<ScreeningDto> expectedList = new ArrayList<>();
        expectedList.add(screeningDto);
        
        Room room = new Room("room1", 10, 10);
        Movie movie = new Movie("movie1", "drama", 100);
        Screening screening = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        List<Screening> expectedEntityList = new ArrayList<>();
        expectedEntityList.add(screening);
        when(screeningRepository.findAll()).thenReturn(expectedEntityList);
        when(movieService.convertEntityToDto(movie)).thenReturn(movieDto);
        when(roomService.convertEntityToDto(room)).thenReturn(roomDto);
        
        
        List<ScreeningDto> result = underTest.getScreeningList();
        assertEquals(expectedList, result);
    }

    /**
     * Test of createScreening method, of class ScreeningServiceImpl.
     */
    @Test
    public void testCreateScreeningWithCorrectRoomAndMovieAndNoOverlaps() {
        Room room = new Room("room1", 10, 10);
        Movie movie = new Movie("movie1", "drama", 100);
        Screening expected = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        when(movieRepository.findByTitle("movie1")).thenReturn(Optional.of(movie));
        when(roomRepository.findByName("room1")).thenReturn(Optional.of(room));
        when(screeningRepository.findByRoom_Name("room1")).thenReturn(Collections.EMPTY_LIST);
        
        underTest.createScreening("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        
        verify(screeningRepository).save(expected);
    }
    
    
    @Test
    public void testCreateScreeningWithIncorrectRoom() {
        Movie movie = new Movie("movie1", "drama", 100);
        when(movieRepository.findByTitle("movie1")).thenReturn(Optional.of(movie));
        when(roomRepository.findByName("room1")).thenReturn(Optional.empty());
        Screening s = new Screening();
        
        underTest.createScreening("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        
        verifyNoMoreInteractions(screeningRepository);
    }

    /**
     * Test of deleteScreening method, of class ScreeningServiceImpl.
     */
    @Test
    public void testDeleteScreening() {
        Room room = new Room("room1", 10, 10);
        Movie movie = new Movie("movie1", "drama", 100);
        Screening expected = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        List<Screening> expectedList = new ArrayList<>();
        expectedList.add(expected);
        when(screeningRepository.findByMovie_TitleAndRoom_NameAndStartDate("movie1", "room1", 
                LocalDateTime.of(2000, Month.MARCH, 1, 12, 0))).thenReturn(expectedList);
        
        underTest.deleteScreening("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        
        verify(screeningRepository).delete(expected);
    }

    @Test
    public void testIsScreeningPossibleWithNoOverlap() {
        Room room = new Room("room1", 10, 10);
        Movie movie = new Movie("movie1", "drama", 100);
        Movie movie2 = new Movie("movie2", "drama", 100);
        Screening screening = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        Screening otherScreening = new Screening(movie2, room, LocalDateTime.of(2000, Month.MARCH, 1, 14, 0));
        List<Screening> otherScreenings = new ArrayList<>();
        otherScreenings.add(otherScreening);
 
        when(screeningRepository.findByRoom_Name("room1")).thenReturn(otherScreenings);
        boolean expected = true;
        
        boolean actual = underTest.isScreeningPossible(screening);
        underTest.createScreening("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsScreeningPossibleWithOverlapAtEnd() {
        Room room = new Room("room1", 10, 10);
        Movie movie = new Movie("movie1", "drama", 100);
        Movie movie2 = new Movie("movie2", "drama", 100);
        Screening screening = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        Screening otherScreening = new Screening(movie2, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 30));
        List<Screening> otherScreenings = new ArrayList<>();
        otherScreenings.add(otherScreening);
 
        when(screeningRepository.findByRoom_Name("room1")).thenReturn(otherScreenings);
        boolean expected = false;
        
        boolean actual = underTest.isScreeningPossible(screening);
        underTest.createScreening("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsScreeningPossibleWithOverlapAtStart() {
        Room room = new Room("room1", 10, 10);
        Movie movie = new Movie("movie1", "drama", 100);
        Movie movie2 = new Movie("movie2", "drama", 100);
        Screening screening = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        Screening otherScreening = new Screening(movie2, room, LocalDateTime.of(2000, Month.MARCH, 1, 11, 0));
        List<Screening> otherScreenings = new ArrayList<>();
        otherScreenings.add(otherScreening);
 
        when(screeningRepository.findByRoom_Name("room1")).thenReturn(otherScreenings);
        boolean expected = false;
        
        boolean actual = underTest.isScreeningPossible(screening);
        underTest.createScreening("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsScreeningPossibleWithBreakOverlap() {
        Room room = new Room("room1", 10, 10);
        Movie movie = new Movie("movie1", "drama", 100);
        Movie movie2 = new Movie("movie2", "drama", 100);
        Screening screening = new Screening(movie, room, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        Screening otherScreening = new Screening(movie2, room, LocalDateTime.of(2000, Month.MARCH, 1, 10, 15));
        List<Screening> otherScreenings = new ArrayList<>();
        otherScreenings.add(otherScreening);
 
        when(screeningRepository.findByRoom_Name("room1")).thenReturn(otherScreenings);
        boolean expected = false;
        
        boolean actual = underTest.isScreeningPossible(screening);
        underTest.createScreening("movie1", "room1", LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        
        assertEquals(expected, actual);
    }
}
