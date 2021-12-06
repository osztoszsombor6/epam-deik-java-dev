/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
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
public class MovieServiceImplTest {
    
    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private MovieService underTest = new MovieServiceImpl(movieRepository);

    /**
     * Test of getMovieList method, of class MovieServiceImpl.
     */
    @Test
    public void testGetMovieList() {
        MovieDto expected = new MovieDto("movie1", "drama", 100);
        List<MovieDto> expectedList = new ArrayList<>();
        expectedList.add(expected);
        Movie expectedEntity = new Movie("movie1", "drama", 100);
        List<Movie> expectedEntityList = new ArrayList<>();
        expectedEntityList.add(expectedEntity);
        when(movieRepository.findAll()).thenReturn(expectedEntityList);
        List<MovieDto> result = underTest.getMovieList();
        assertEquals(expectedList, result);
    }

    @Test
    public void testDeleteMovie() {
        Movie movie = new Movie("movie1", "drama", 100);
        when(movieRepository.findByTitle("movie1")).thenReturn(Optional.of(movie));
        
        underTest.deleteMovie("movie1");
        verify(movieRepository).delete(movie);
    }
    
    @Test
    public void testUpdateMovie() {
        Movie movie = new Movie("movie1", "drama", 100);
        MovieDto movieDto = new MovieDto("movie1", "asd", 5);
        when(movieRepository.findByTitle("movie1")).thenReturn(Optional.of(movie));
        
        Movie resultMovie = new Movie("movie1", "asd", 5);
        
        underTest.updateMovie(movieDto);
        verify(movieRepository).save(resultMovie);
    }
    
    @Test
    public void testCreateMovie() {
        MovieDto movieDto = new MovieDto("movie1", "asd", 5);
        
        Movie resultMovie = new Movie("movie1", "asd", 5);
        
        underTest.createMovie(movieDto);
        verify(movieRepository).save(resultMovie);
    }
//
//    /**
//     * Test of convertEntityToDto method, of class MovieServiceImpl.
//     */
    @Test
    public void testConvertEntityToDto() {
        Optional<Movie> movie1 = Optional.of(new Movie("movie1", "drama", 100));
        Optional<MovieDto> expected = Optional.of(new MovieDto("movie1", "drama", 100));
        when(movieRepository.findByTitle("movie1")).thenReturn(movie1);
        
        Optional<MovieDto> actual = underTest.getMovieByTitle("movie1");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConvertEntityToDtoIfMovieIsNotFound() {
        Optional<MovieDto> expected = Optional.empty();
        when(movieRepository.findByTitle("movie1")).thenReturn(Optional.empty());
        
        Optional<MovieDto> actual = underTest.getMovieByTitle("movie1");
        
        assertEquals(expected, actual);
    }
    
}
