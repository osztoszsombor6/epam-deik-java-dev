package com.epam.training.ticketservice.ui.command;


import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.converter.LocalDateTime2StringConverter;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class UserCommand {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final LocalDateTime2StringConverter converter;

    public UserCommand(UserService userService, BookingRepository bookingRepository, 
                        LocalDateTime2StringConverter converter) {
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.converter = converter;
    }

    @ShellMethod(key = "sign in", value = "User login")
    public String login(String username, String password) {
        
        Optional<UserDto> user = userService.login(username, password);
        if (user.isEmpty()) {
            return "Login failed due to incorrect credentials";
        }
        return user.get() + " is logged in!";
    }
    
    @ShellMethod(key = "sign in privileged", value = "User login")
    public String loginPrivileged(String username, String password) {   
        if (User.Role.ADMIN.equals(userService.getUserRole(username))) {
            Optional<UserDto> user = userService.login(username, password);
            if (user.isEmpty()) {
                return "Login failed due to incorrect credentials";
            }

            return user.get() + " is logged in!";
        }
        return "You are not an admin!";
    }

    @ShellMethod(key = "sign out", value = "User logout")
    public String logout() {
        Optional<UserDto> user = userService.logout();
        if (user.isEmpty()) {
            return "You need to login first!";
        }
        return user.get() + " is logged out!";
    }


    @ShellMethod(key = "sign up", value = "User registration")
    public String registerUser(String userName, String password) {
        try {
            userService.registerUser(userName, password);
            return "Registration was successful!";
        } catch (Exception e) {
            return "Registration failed!";
        }
    }
    
    @ShellMethod(key = "describe account", value = "Get user information")
    public String printUserInformation() {
        Optional<UserDto> userDto = userService.getLoggedInUser();
        if (userDto.isEmpty()) {
            return "You are not signed in";
        }
        if (userDto.get().getRole() == User.Role.ADMIN) {
            return "Signed in with privileged account '" + userDto.get().getUsername() + "'";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Signed in with account '");
        sb.append(userDto.get().getUsername());
        sb.append("'\n");
        
        List<Booking> bookings = bookingRepository.findByUser_Username(userDto.get().getUsername());
        if (bookings.isEmpty()) {
            sb.append("You have not booked any tickets yet");
        } else {
            boolean firstBooking = true;
            sb.append("Your previous bookings are\n");
            for (Booking booking : bookings) {
                if (!firstBooking) {
                    sb.append("\n");
                }
                sb.append("Seats ");
                boolean firstSeat = true;
                for (Seat seat : booking.getSeats()) {
                    if (!firstSeat) {
                        sb.append(", ");
                    }
                    sb.append("(");
                    sb.append(seat.getRow());
                    sb.append(",");
                    sb.append(seat.getCol());
                    sb.append(")");
                    firstSeat = false;
                }
                sb.append(" on ");
                sb.append(booking.getScreening().getMovie().getTitle());
                sb.append(" in room ");
                sb.append(booking.getScreening().getRoom().getName());
                sb.append(" starting at ");
                sb.append(converter.convert(booking.getScreening().getStartDate()));
                sb.append(" for ");
                sb.append(booking.getPrice());
                sb.append(" HUF");
                firstBooking = false;
            }
        }
        
        return sb.toString();
    }
}
