package com.epam.training.ticketservice.core.price;


import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import java.time.LocalDateTime;
import java.util.List;

public interface PriceService {

    Integer getBasePrice();
    
    void updateBasePrice(Integer price);

    public Integer getPrice(Screening screening, List<Seat> seatList);

    public void createPriceComponent(String name, Integer price);
    
    public void attachPriceComponentToRoom(String priceName, String roomName);
    
    public void attachPriceComponentToMovie(String priceName, String movieTitle);
    
    public void attachPriceComponentToScreening(String priceName, String movieTitle, 
                                                String roomName, LocalDateTime date);
}
