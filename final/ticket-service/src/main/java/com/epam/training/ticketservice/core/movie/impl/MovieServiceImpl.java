package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    
    @Override
    public List<MovieDto> getMovieList() {
        return movieRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<MovieDto> getMovieByTitle(String movieTitle) {
        return convertEntityToDto(movieRepository.findByTitle(movieTitle));
    }

    @Override
    public void createMovie(MovieDto movieDto) {
        Objects.requireNonNull(movieDto, "Movie cannot be null");
        Objects.requireNonNull(movieDto.getTitle(), "Movie Title cannot be null");
        Objects.requireNonNull(movieDto.getGenre(), "Movie Genre cannot be null");
        Objects.requireNonNull(movieDto.getLength(), "Movie Length cannot be null");
        Movie movie = new Movie(movieDto.getTitle(),
            movieDto.getGenre(),
            movieDto.getLength());
        movieRepository.save(movie);
    }
    
    @Override
    public void updateMovie(MovieDto movieDto) {
        Optional<Movie> movie = movieRepository.findByTitle(movieDto.getTitle());
        movie.ifPresent(m -> {
            m.setGenre(movieDto.getGenre());
            m.setLength(movieDto.getLength());
            movieRepository.save(m);
        });
    }

    @Override
    public void deleteMovie(String title) {
        Optional<Movie> movie = movieRepository.findByTitle(title);
        movie.ifPresent(m -> {
            movieRepository.delete(m);
        });
    }
    

    public MovieDto convertEntityToDto(Movie movie) {
        return MovieDto.builder()
            .withTitle(movie.getTitle())
            .withGenre(movie.getGenre())
            .withLength(movie.getLength())
            .build();
    }

    private Optional<MovieDto> convertEntityToDto(Optional<Movie> movie) {
        return movie.isEmpty() ? Optional.empty() : Optional.of(convertEntityToDto(movie.get()));
    }

    
}
