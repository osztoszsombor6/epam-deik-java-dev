package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import java.time.LocalDateTime;
import java.util.Objects;


public class ScreeningDto {
    
    private final MovieDto movieDto;
    private final RoomDto roomDto;
    private final LocalDateTime date;

    public ScreeningDto(MovieDto movieDto, RoomDto roomDto, LocalDateTime date) {
        this.movieDto = movieDto;
        this.roomDto = roomDto;
        this.date = date;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public MovieDto getMovieDto() {
        return movieDto;
    }

    public RoomDto getRoomDto() {
        return roomDto;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.movieDto);
        hash = 23 * hash + Objects.hashCode(this.roomDto);
        hash = 23 * hash + Objects.hashCode(this.date);
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
        final ScreeningDto other = (ScreeningDto) obj;
        if (!Objects.equals(this.movieDto, other.movieDto)) {
            return false;
        }
        if (!Objects.equals(this.roomDto, other.roomDto)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ScreeningDto{" + "movieDto=" + movieDto + ", roomDto=" + roomDto + ", date=" + date + '}';
    }
    
    public static class Builder {
        private MovieDto movieDto;
        private RoomDto roomDto;
        private LocalDateTime date;

        public Builder withMovie(MovieDto movieDto) {
            this.movieDto = movieDto;
            return this;
        }

        public Builder withRoom(RoomDto roomDto) {
            this.roomDto = roomDto;
            return this;
        }
        
        public Builder withDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public ScreeningDto build() {
            return new ScreeningDto(movieDto, roomDto, date);
        }
    }
}
