package com.epam.training.ticketservice.core.movie.model;

import java.util.Objects;

public class MovieDto {

    private final String title;
    private final String genre;
    private final Integer length;

    public MovieDto(String title, String genre, Integer length) {
        this.title = title;
        this.genre = genre;
        this.length = length;
    }


    public static Builder builder() {
        return new Builder();
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getLength() {
        return length;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.title);
        hash = 41 * hash + Objects.hashCode(this.genre);
        hash = 41 * hash + Objects.hashCode(this.length);
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
        final MovieDto other = (MovieDto) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.genre, other.genre)) {
            return false;
        }
        if (!Objects.equals(this.length, other.length)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MovieDto{" + "title=" + title + ", genre=" + genre + ", length=" + length + '}';
    }


    public static class Builder {
        private String title;
        private String genre;
        private Integer length;

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withGenre(String genre) {
            this.genre = genre;
            return this;
        }
        
        public Builder withLength(Integer length) {
            this.length = length;
            return this;
        }

        public MovieDto build() {
            return new MovieDto(title, genre, length);
        }
    }
}