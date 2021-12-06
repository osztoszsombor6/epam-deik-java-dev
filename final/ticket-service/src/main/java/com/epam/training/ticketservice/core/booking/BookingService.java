package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.model.InvalidSeatException;
import com.epam.training.ticketservice.core.booking.model.SeatAlreadyBookedException;
import java.time.LocalDateTime;


public interface BookingService {
    
    BookingDto createBooking(String movieTitle, String roomName, LocalDateTime date, String seats) 
            throws InvalidSeatException, SeatAlreadyBookedException;
    
    public Integer showPrice(String movieTitle, String roomName, LocalDateTime date, String seats) 
            throws InvalidSeatException, SeatAlreadyBookedException;
}
