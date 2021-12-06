package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;


@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final RoomRepository roomRepository;
    private final RoomService roomService;
    
    private final MovieRepository movieRepository;
    private final MovieService movieService;
    
    private final ScreeningRepository screeningRepository;

    public ScreeningServiceImpl(RoomRepository roomRepository, RoomService roomService, 
                MovieRepository movieRepository, MovieService movieService, ScreeningRepository screeningRepository) {
        this.roomRepository = roomRepository;
        this.roomService = roomService;
        this.movieRepository = movieRepository;
        this.movieService = movieService;
        this.screeningRepository = screeningRepository;
    }

    
    @Override
    public List<ScreeningDto> getScreeningList() {
        return screeningRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void createScreening(String movieTitle, String roomName, LocalDateTime date) {
        Optional<Room> room = roomRepository.findByName(roomName);
        
        room.ifPresent(r -> {
            Optional<Movie> movie = movieRepository.findByTitle(movieTitle);
            movie.ifPresent(m -> {
                Screening s = new Screening(m, r, date);
                if (isScreeningPossible(s)) {
                    screeningRepository.save(s);
                }
            });
        });
    }

    @Override
    public void deleteScreening(String movieTitle, String roomName, LocalDateTime date) {
        List<Screening> screenings = 
                screeningRepository.findByMovie_TitleAndRoom_NameAndStartDate(movieTitle, roomName, date);
        screenings.forEach(s -> screeningRepository.delete(s));
        
    }
    
    @Override
    public ScreeningDto convertEntityToDto(Screening screening) {
        return ScreeningDto.builder()
            .withMovie(movieService.convertEntityToDto(screening.getMovie()))
            .withRoom(roomService.convertEntityToDto(screening.getRoom()))
            .withDate(screening.getStartDate())
            .build();
    }

    public boolean isScreeningPossible(Screening screening) {
        Room room = screening.getRoom();
        List<Screening> screeningsInRoom = screeningRepository.findByRoom_Name(room.getName());
        
        for (Screening screeningToCheck : screeningsInRoom) {
            if (screeningToCheck.getEndDate().isAfter(screening.getStartDate()) 
                    && screeningToCheck.getStartDate().isBefore(screening.getEndDate())) {
                System.out.println("There is an overlapping screening");
                return false;
            }
            if (screeningToCheck.getEndDate().isBefore(screening.getStartDate()) 
                    && screeningToCheck.getEndDate().plusMinutes(10).isAfter(screening.getStartDate())) {
                System.out.println("This would start in the break period after another screening in this room");
                return false;
            }
        }
        return true;
    }

}
