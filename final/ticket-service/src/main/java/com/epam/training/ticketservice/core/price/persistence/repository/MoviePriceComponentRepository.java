package com.epam.training.ticketservice.core.price.persistence.repository;


import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.price.persistence.entity.MoviePriceComponent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviePriceComponentRepository extends JpaRepository<MoviePriceComponent, Integer> {
    
    List<MoviePriceComponent> findByMovie(Movie movie);
    
}
