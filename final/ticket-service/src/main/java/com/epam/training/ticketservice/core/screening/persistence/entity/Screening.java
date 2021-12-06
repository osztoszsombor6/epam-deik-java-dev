package com.epam.training.ticketservice.core.screening.persistence.entity;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@Entity
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @OneToOne
    private Movie movie;
    
    @OneToOne
    private Room room;
    
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    public Screening() {
    }

    public Screening(Movie movie, Room room, LocalDateTime date) {
        this.movie = movie;
        this.room = room;
        this.startDate = date;
        this.endDate = startDate.plusMinutes(movie.getLength());
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.movie);
        hash = 41 * hash + Objects.hashCode(this.room);
        hash = 41 * hash + Objects.hashCode(this.startDate);
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
        final Screening other = (Screening) obj;
        if (!Objects.equals(this.movie, other.movie)) {
            return false;
        }
        if (!Objects.equals(this.room, other.room)) {
            return false;
        }
        if (!Objects.equals(this.startDate, other.startDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Screening{" + "id=" + id + ", movie=" + movie.getTitle() + ", room=" + room.getName() 
                + ", startDate=" + startDate + ", endDate=" + endDate + "}";
    }
    
    
}
