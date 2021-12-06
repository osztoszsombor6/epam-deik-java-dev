package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.price.PriceService;
import com.epam.training.ticketservice.core.price.persistence.repository.PriceRepository;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;


@ShellComponent
public class PriceCommand {
    
    private UserService userService;
    private PriceService priceService;
    private RoomService roomService;
    private PriceRepository priceRepository;

    public PriceCommand(UserService userService, PriceService priceService,
            RoomService roomService, PriceRepository priceRepository) {
        this.userService = userService;
        this.priceService = priceService;
        this.roomService = roomService;
        this.priceRepository = priceRepository;
    }
    
    
    
    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update base price", value = "Update base price")
    public void updateBasePrice(Integer newPrice) {
        priceService.updateBasePrice(newPrice);
    }
    
    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create price component", value = "Create new price component")
    public void createPriceComponent(String name, Integer price) {
        priceService.createPriceComponent(name, price);
    }
    
    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to room", value = "Attach a price component to a room")
    public void attachPriceComponentToRoom(String priceName, String roomName) {
        priceService.attachPriceComponentToRoom(priceName, roomName);
    }
    
    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to movie", value = "Attach a price component to a movie")
    public void attachPriceComponentToMovie(String priceName, String movieTitle) {
        priceService.attachPriceComponentToMovie(priceName, movieTitle);
    }
    
    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to screening", value = "Attach a price component to a screening")
    public void attachPriceComponentToScreening(String priceName, String movieTitle,
                                                String roomName, LocalDateTime startDate) {
        priceService.attachPriceComponentToScreening(priceName, movieTitle, roomName, startDate);
    }
    
    private Availability isAvailable() {
        Optional<UserDto> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin");
    }
}
