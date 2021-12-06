package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.model.InvalidSeatException;
import com.epam.training.ticketservice.core.booking.model.SeatAlreadyBookedException;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
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
public class BookingCommand {
    
    private final BookingService bookingService;
    private final UserService userService;

    public BookingCommand(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }
    
    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "book", value = "Book seats on a screening")
    public String createBooking(String movieTitle, String roomName, LocalDateTime date, String seats) {
        BookingDto bookingDto;
        try {
            bookingDto = bookingService.createBooking(movieTitle, roomName, date, seats);
        } catch (InvalidSeatException ex) {
            return ("Seat (" + ex.getRow() + "," + ex.getCol() + ") does not exist in this room");
        } catch (SeatAlreadyBookedException ex) {
            return ("Seat (" + ex.getRow() + "," + ex.getCol() + ") is already taken");
        }
        StringBuilder result = new StringBuilder();
        if (bookingDto != null) {
            result.append("Seats booked: ");
            boolean notFirst = false;
            for (SeatDto seatDto: bookingDto.getSeatDtoList()) {
                if (notFirst) {
                    result.append(", ");
                }
                result.append("(");
                result.append(seatDto.getRow());
                result.append(",");
                result.append(seatDto.getCol());
                result.append(")");
                notFirst = true;
            }
            result.append("; the price for this booking is ");
            result.append(bookingDto.getPrice());
            result.append(" HUF");
        }
        
        return result.toString();
    }
    
    @ShellMethod(key = "show price for", value = "Show price for booking")
    public String showPrice(String movieTitle, String roomName, LocalDateTime startDate, String seats) {
        Integer price;
        try {
            price = bookingService.showPrice(movieTitle, roomName, startDate, seats);
        } catch (InvalidSeatException ex) {
            return ("Seat (" + ex.getRow() + "," + ex.getCol() + ") does not exist in this room");
        } catch (SeatAlreadyBookedException ex) {
            return ("Seat (" + ex.getRow() + "," + ex.getCol() + ") is already taken");
        }
        return "The price for this booking would be " + price + " HUF";
    }
    
    private Availability isAvailable() {
        Optional<UserDto> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() != User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("Only logged in non-admin users can book");
    }
    
}
