package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.model.InvalidSeatException;
import com.epam.training.ticketservice.core.booking.model.SeatAlreadyBookedException;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.booking.persistence.repository.SeatRepository;
import com.epam.training.ticketservice.core.price.PriceService;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {
    
    private ScreeningRepository screeningRepository;
    private ScreeningService screeningService;
    private UserRepository userRepository;
    private UserService userService;
    private BookingRepository bookingRepository;
    private SeatRepository seatRepository;
    private PriceService priceService;

    public BookingServiceImpl(ScreeningRepository screeningRepository, UserRepository userRepository, 
                              UserService userService, BookingRepository bookingRepository, 
                              ScreeningService screeningService, SeatRepository seatRepository,
                              PriceService priceService) {
        this.screeningRepository = screeningRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.screeningService = screeningService;
        this.seatRepository = seatRepository;
        this.priceService = priceService;
    }
    
    List<Seat> getSeatList(String seats) {
        List<Seat> result = new ArrayList<>();
        String[] seatsStr = seats.split(" ");
        for (String seatStr: seatsStr) {
            String[] pos = seatStr.split(",");
            Seat seat = new Seat();
            seat.setRow(Integer.parseInt(pos[0]));
            seat.setCol(Integer.parseInt(pos[1]));
            result.add(seat);
        }
        return result;
    }

    @Override
    public BookingDto createBooking(String movieTitle, String roomName, LocalDateTime date, String seats) 
            throws InvalidSeatException, SeatAlreadyBookedException {
        List<Screening> screeningList = 
                screeningRepository.findByMovie_TitleAndRoom_NameAndStartDate(movieTitle, roomName, date);
        BookingDto createdBookingDto = null;
        if (!screeningList.isEmpty()) {
            Screening screening = screeningList.get(0);
            Optional<UserDto> userDto = userService.getLoggedInUser();
            UserDto udto = userDto.get();
            Optional<User> u = userRepository.findByUsername(udto.getUsername());
            User user = u.get();
            List<Seat> seatList = getSeatList(seats);
            Integer price = priceService.getPrice(screening, seatList);
            Booking booking = new Booking(user, screening, seatList, price);
            checkBookingIsPossible(booking);
            booking = bookingRepository.save(booking);
            createdBookingDto = convertEntityToDto(booking);
        }
        return createdBookingDto;
    }
    
    @Override
    public Integer showPrice(String movieTitle, String roomName, LocalDateTime date, String seats) 
                                            throws InvalidSeatException, SeatAlreadyBookedException {
        List<Screening> screeningList = 
                screeningRepository.findByMovie_TitleAndRoom_NameAndStartDate(movieTitle, roomName, date);
        Integer price = 0;
        if (!screeningList.isEmpty()) {
            Screening screening = screeningList.get(0);
            List<Seat> seatList = getSeatList(seats);
            price = priceService.getPrice(screening, seatList);
        }
        return price;
    }
    
    private BookingDto convertEntityToDto(Booking booking) {
        User user = booking.getUser();
        UserDto userDto = new UserDto(user.getUsername(), user.getRole());
        ScreeningDto screeningDto = screeningService.convertEntityToDto(booking.getScreening());
        List<SeatDto> seatDtoList = new ArrayList<>();
        for (Seat seat : booking.getSeats()) {
            SeatDto seatDto = SeatDto.builder()
                                .withRow(seat.getRow())
                                .withCol(seat.getCol())
                                .build();
            seatDtoList.add(seatDto);
        }
        return BookingDto.builder()
            .withUser(userDto)
            .withScreening(screeningDto)
            .withPrice(booking.getPrice())
            .withSeats(seatDtoList)
            .build();
    }
    
    private void checkBookingIsPossible(Booking booking) throws InvalidSeatException, SeatAlreadyBookedException {
        checkSeatIsValid(booking);
        List<Seat> seatList = seatRepository.findByBooking_Screening_Id(booking.getScreening().getId());
        for (Seat seat : booking.getSeats()) {
            for (Seat bookedSeat : seatList) {
                if (seat.getRow().equals(bookedSeat.getRow()) && seat.getCol().equals(bookedSeat.getCol())) {
                    throw new SeatAlreadyBookedException(seat.getRow(),seat.getCol());
                }
            }
        }
    }
    
    private void checkSeatIsValid(Booking booking) throws InvalidSeatException {
        Room room = booking.getScreening().getRoom();
        for (Seat seat : booking.getSeats()) {
            if (seat.getRow() < 1 || seat.getCol() < 1 
                    || seat.getRow() > room.getRowNum() || seat.getCol() > room.getColNum()) {
                throw new InvalidSeatException(seat.getRow(), seat.getCol());
            }
        }
    }
}
