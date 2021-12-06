package com.epam.training.ticketservice.core.movie;


import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import java.util.List;
import java.util.Optional;

public interface MovieService {

    List<MovieDto> getMovieList();

    Optional<MovieDto> getMovieByTitle(String movieTitle);

    void createMovie(MovieDto movie);
    
    void updateMovie(MovieDto movie);
    
    void deleteMovie(String title);
    
    public MovieDto convertEntityToDto(Movie movie);
}