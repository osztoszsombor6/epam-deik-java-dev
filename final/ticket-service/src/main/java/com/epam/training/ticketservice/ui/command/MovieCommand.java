package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;


@ShellComponent
public class MovieCommand {
    
    private final MovieService movieService;
    private final UserService userService;

    public MovieCommand(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    
    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create movie", value = "Create new movie")
    public MovieDto createMovie(String title, String genre, Integer length) {
        MovieDto movie = MovieDto.builder()
            .withTitle(title)
            .withGenre(genre)
            .withLength(length)
            .build();
        movieService.createMovie(movie);
        return movie;
    }
    
    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update movie", value = "Update movie")
    public MovieDto updateMovie(String title, String genre, Integer length) {
        MovieDto movie = MovieDto.builder()
            .withTitle(title)
            .withGenre(genre)
            .withLength(length)
            .build();
        movieService.updateMovie(movie);
        return movie;
    }
    
    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete movie", value = "Delete movie")
    public void deleteMovie(String title) {
        movieService.deleteMovie(title);
    }
    
    @ShellMethod(key = "list movies", value = "List movies")
    public String listMovies() {
        List<MovieDto> movieList = movieService.getMovieList();
        if (movieList.isEmpty()) {
            return "There are no movies at the moment";
        }
        StringBuilder s = new StringBuilder();
        movieList.forEach(m -> {
            if (s.length() > 0) {
                s.append("\n");
            }
            s.append(m.getTitle());
            s.append(" (");
            s.append(m.getGenre());
            s.append(", ");
            s.append(m.getLength());
            s.append(" minutes)");
        });
        return s.toString();
    }
    
    private Availability isAvailable() {
        Optional<UserDto> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }
}
