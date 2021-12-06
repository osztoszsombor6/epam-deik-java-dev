package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ScreeningService {
    List<ScreeningDto> getScreeningList();

    void createScreening(String movieTitle, String roomName, LocalDateTime date);
    
    void deleteScreening(String movieTitle, String roomName, LocalDateTime date);
    
    public ScreeningDto convertEntityToDto(Screening screening);
}
