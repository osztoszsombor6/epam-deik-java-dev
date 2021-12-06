/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
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
public class MovieCommandTest {
    
    private final MovieService movieService = mock(MovieService.class);
    private final UserService userService = mock(UserService.class);
    
    private MovieCommand underTest = new MovieCommand(movieService, userService);

    /**
     * Test of createMovie method, of class MovieCommand.
     */
    @Test
    public void testCreateMovie() {
        MovieDto movieDto = new MovieDto("movie1", "drama", 10);
        
        
        MovieDto actual = underTest.createMovie("movie1", "drama", 10);

        verify(movieService).createMovie(movieDto);
        assertEquals(movieDto, actual);
    }

    /**
     * Test of updateMovie method, of class MovieCommand.
     */
    @Test
    public void testUpdateMovie() {
        MovieDto movieDto = new MovieDto("movie1", "drama", 10);
        
        
        MovieDto actual = underTest.updateMovie("movie1", "drama", 10);

        verify(movieService).updateMovie(movieDto);
        assertEquals(movieDto, actual);
    }

    /**
     * Test of deleteMovie method, of class MovieCommand.
     */
    @Test
    public void testDeleteMovie() {
        underTest.deleteMovie("movie1");

        verify(movieService).deleteMovie("movie1");
    }

    /**
     * Test of listMovies method, of class MovieCommand.
     */
    @Test
    public void testListMoviesWithNoSavedMovies() {
        when(movieService.getMovieList()).thenReturn(Collections.EMPTY_LIST);
        String expected = "There are no movies at the moment";
        
        String actual = underTest.listMovies();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testListMoviesWithSavedMovie() {
        MovieDto movie1 = new MovieDto("movie1", "drama", 10);
        List<MovieDto> movieList = new ArrayList<>();
        movieList.add(movie1);
        when(movieService.getMovieList()).thenReturn(movieList);
        String expected = "movie1 (drama, 10 minutes)";
        
        String actual = underTest.listMovies();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testListMoviesWithSavedMovies() {
        MovieDto movie1 = new MovieDto("movie1", "drama", 10);
        MovieDto movie2 = new MovieDto("movie2", "action", 100);
        List<MovieDto> movieList = new ArrayList<>();
        movieList.add(movie1);
        movieList.add(movie2);
        when(movieService.getMovieList()).thenReturn(movieList);
        String expected = "movie1 (drama, 10 minutes)\n" +
                    "movie2 (action, 100 minutes)";
        
        String actual = underTest.listMovies();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsAvailableUser() throws NoSuchMethodException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException {
        UserDto userDto = new UserDto("user", User.Role.USER);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        Availability expected = Availability.unavailable("You are not an admin!");
        
        Method isAvailable = MovieCommand.class.getDeclaredMethod("isAvailable");
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
        
        Method isAvailable = MovieCommand.class.getDeclaredMethod("isAvailable");
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
        
        Method isAvailable = MovieCommand.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);
        
        assertEquals(expected.isAvailable(), actual.isAvailable());
        assertEquals(expected.getReason(), actual.getReason());
    }
    
}
