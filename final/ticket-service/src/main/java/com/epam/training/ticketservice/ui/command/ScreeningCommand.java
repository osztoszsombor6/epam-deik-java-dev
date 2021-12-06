package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.converter.LocalDateTime2StringConverter;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;


@ShellComponent
public class ScreeningCommand {
    
    private final LocalDateTime2StringConverter converter;
    private final ScreeningService screeningService;
    private final UserService userService;

    public ScreeningCommand(LocalDateTime2StringConverter converter, 
                            ScreeningService screeningService, UserService userService) {
        this.converter = converter;
        this.screeningService = screeningService;
        this.userService = userService;
    }



    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Create new screening")
    public void createScreening(String movieTitle, String roomName, LocalDateTime startDate) {
        screeningService.createScreening(movieTitle, roomName, startDate);
    }
    
    
    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Delete screening")
    public void deleteScreening(String movieTitle, String roomName, LocalDateTime startDate) {
        screeningService.deleteScreening(movieTitle, roomName, startDate);
    }
    
    @ShellMethod(key = "list screenings", value = "List screenings")
    public String listScreenings() {
        List<ScreeningDto> screeningList = screeningService.getScreeningList();
        if (screeningList.isEmpty()) {
            return "There are no screenings";
        }
        StringBuilder s = new StringBuilder();
        screeningList.forEach(scr -> {
            if (s.length() > 0) {
                s.append("\n");
            }
            MovieDto m = scr.getMovieDto();
            RoomDto r = scr.getRoomDto();
            s.append(m.getTitle());
            s.append(" (");
            s.append(m.getGenre());
            s.append(", ");
            s.append(m.getLength());
            s.append(" minutes), screened in room ");
            s.append(r.getName());
            s.append(", at ");
            s.append(converter.convert(scr.getDate()));
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
