package com.epam.training.ticketservice.core.price.persistence.entity;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class MoviePriceComponent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @ManyToOne
    private PriceComponent priceComponent;
    
    @ManyToOne
    private Movie movie;

    public PriceComponent getPriceComponent() {
        return priceComponent;
    }

    public void setPriceComponent(PriceComponent priceComponent) {
        this.priceComponent = priceComponent;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.priceComponent);
        hash = 47 * hash + Objects.hashCode(this.movie);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MoviePriceComponent other = (MoviePriceComponent) obj;
        if (!Objects.equals(this.priceComponent, other.priceComponent)) {
            return false;
        }
        if (!Objects.equals(this.movie, other.movie)) {
            return false;
        }
        return true;
    }
    
    
}
