package com.epam.training.ticketservice.core.booking.model;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.user.model.UserDto;
import java.util.List;

public class BookingDto {
    
    private final UserDto userDto;
    private final ScreeningDto screeningDto;
    private final List<SeatDto> seatDtoList;
    private final int price;

    public BookingDto(UserDto userDto, ScreeningDto screeningDto, List<SeatDto> seatDtoList, int price) {
        this.userDto = userDto;
        this.screeningDto = screeningDto;
        this.seatDtoList = seatDtoList;
        this.price = price;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public ScreeningDto getScreeningDto() {
        return screeningDto;
    }

    public List<SeatDto> getSeatDtoList() {
        return seatDtoList;
    }

    public int getPrice() {
        return price;
    }
    
    public static class Builder {
        private UserDto userDto;
        private ScreeningDto screeningDto;
        private List<SeatDto> seatDtoList;
        private int price;

        public Builder withUser(UserDto userDto) {
            this.userDto = userDto;
            return this;
        }

        public Builder withScreening(ScreeningDto screeningDto) {
            this.screeningDto = screeningDto;
            return this;
        }
        
        public Builder withSeats(List<SeatDto> seatDtoList) {
            this.seatDtoList = seatDtoList;
            return this;
        }
        
        public Builder withPrice(int price) {
            this.price = price;
            return this;
        }

        public BookingDto build() {
            return new BookingDto(userDto, screeningDto, seatDtoList, price);
        }
    }
    
}
