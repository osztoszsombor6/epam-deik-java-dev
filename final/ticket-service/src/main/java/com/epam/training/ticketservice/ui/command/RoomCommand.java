package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
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
public class RoomCommand {
    
    private final RoomService roomService;
    private final UserService userService;

    public RoomCommand(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    

    
    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create room", value = "Create new room")
    public RoomDto createRoom(String name, Integer rowNum, Integer colNum) {
        RoomDto room = RoomDto.builder()
            .withName(name)
            .withRowNum(rowNum)
            .withColNum(colNum)
            .build();
        roomService.createRoom(room);
        return room;
    }
    
    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update room", value = "Update room")
    public RoomDto updateRoom(String name, Integer rowNum, Integer colNum) {
        RoomDto room = RoomDto.builder()
            .withName(name)
            .withRowNum(rowNum)
            .withColNum(colNum)
            .build();
        roomService.updateRoom(room);
        return room;
    }
    
    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete room", value = "Delete room")
    public void deleteRoom(String name) {
        roomService.deleteRoom(name);
    }
    
    @ShellMethod(key = "list rooms", value = "List movies")
    public String listMovies() {
        List<RoomDto> roomList = roomService.getRoomList();
        if (roomList.isEmpty()) {
            return "There are no rooms at the moment";
        }
        StringBuilder s = new StringBuilder();
        roomList.forEach(r -> {
            if (s.length() > 0) {
                s.append("\n");
            }
            s.append("Room ");
            s.append(r.getName());
            s.append(" with ");
            s.append(r.getColNum() * r.getRowNum());
            s.append(" seats, ");
            s.append(r.getRowNum());
            s.append(" rows and ");
            s.append(r.getColNum());
            s.append(" columns");
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
