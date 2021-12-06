package com.epam.training.ticketservice.core.configuration;


import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;

@Component
@Profile("ci")
public class InMemoryDatabaseInitializer {

    private final UserRepository userRepository;
    
    private final RoomRepository roomRepository;
    
    private final MovieRepository movieRepository;
    
    private final ScreeningRepository screeningRepository;
    
    private final BookingRepository bookingRepository;

    public InMemoryDatabaseInitializer(UserRepository userRepository, 
            RoomRepository roomRepository, MovieRepository movieRepository,
            ScreeningRepository screeningRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.bookingRepository = bookingRepository;
    }


    @PostConstruct
    public void init() {

        User admin = new User("admin", "admin", User.Role.ADMIN);
        userRepository.save(admin);
        /*User user = new User("us", "us", User.Role.USER);
        userRepository.save(user);
        
        Room room1 = new Room("room1", 10, 5);
        Room room2 = new Room("room2", 10, 15);
        roomRepository.save(room1);
        roomRepository.save(room2);
        
        Movie movie1 = new Movie("movie1", "a", 100);
        Movie movie2 = new Movie("movie2", "b", 50);
        movieRepository.save(movie1);
        movieRepository.save(movie2);
        
        Screening screening1 = new Screening(movie1, room1, LocalDateTime.of(2021, 12, 2, 12, 30));
        screeningRepository.save(screening1);
        
        List<Seat> seats = new ArrayList<>();
        seats.add(new Seat(null, 5,5));
        seats.add(new Seat(null, 5,6));
        Booking booking1 = new Booking(user, screening1, seats, 1500);
        bookingRepository.save(booking1);*/
    }
}
