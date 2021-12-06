package com.epam.training.ticketservice.core.price.impl;

import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.price.PriceService;
import com.epam.training.ticketservice.core.price.persistence.entity.MoviePriceComponent;
import com.epam.training.ticketservice.core.price.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.price.persistence.entity.RoomPriceComponent;
import com.epam.training.ticketservice.core.price.persistence.entity.ScreeningPriceComponent;
import com.epam.training.ticketservice.core.price.persistence.repository.MoviePriceComponentRepository;
import com.epam.training.ticketservice.core.price.persistence.repository.PriceRepository;
import com.epam.training.ticketservice.core.price.persistence.repository.RoomPriceComponentRepository;
import com.epam.training.ticketservice.core.price.persistence.repository.ScreeningPriceComponentRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    
    private final RoomPriceComponentRepository roomPriceComponentRepository;
    private final MoviePriceComponentRepository moviePriceComponentRepository;
    private final ScreeningPriceComponentRepository screeningPriceComponentRepository;

    public PriceServiceImpl(PriceRepository priceRepository, RoomRepository roomRepository, 
            MovieRepository movieRepository, ScreeningRepository screeningRepository, 
            RoomPriceComponentRepository roomPriceComponentRepository, 
            MoviePriceComponentRepository moviePriceComponentRepository, 
            ScreeningPriceComponentRepository screeningPriceComponentRepository) {
        this.priceRepository = priceRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.roomPriceComponentRepository = roomPriceComponentRepository;
        this.moviePriceComponentRepository = moviePriceComponentRepository;
        this.screeningPriceComponentRepository = screeningPriceComponentRepository;
    }
    
    
    
    @Override
    public Integer getBasePrice() {
        Optional<PriceComponent> price = priceRepository.findByNameIsNull();
        return price.isPresent() ? price.get().getPrice() : 1500;
    }
    
    @Override
    public void updateBasePrice(Integer newPrice) {
        Optional<PriceComponent> price = priceRepository.findByNameIsNull();
        if (price.isPresent()) {
            PriceComponent basePrice = price.get();
            basePrice.setPrice(newPrice);
            priceRepository.save(basePrice);
        } else {
            PriceComponent basePrice = new PriceComponent(null, newPrice);
            priceRepository.save(basePrice);
        }
    }

    @Override
    public Integer getPrice(Screening screening, List<Seat> seatList) {
        Integer price = getBasePrice() + getAdditionalPrice(screening);
        return price * seatList.size();
    }
     

    private Integer getAdditionalPrice(Screening screening) {
        Integer price = 0;
        price += getRoomAdditionalPrice(screening.getRoom());
        price += getMovieAdditionalPrice(screening.getMovie());
        price += getScreeningAdditionalPrice(screening);
        return price;
    }
    
    private Integer getRoomAdditionalPrice(Room room) {
        Integer price = 0;
        List<RoomPriceComponent> prices = roomPriceComponentRepository.findByRoom(room);
        for (RoomPriceComponent rpc : prices) {
            price += rpc.getPriceComponent().getPrice();
        }
        return price;
    }
    
    private Integer getMovieAdditionalPrice(Movie movie) {
        Integer price = 0;
        List<MoviePriceComponent> prices = moviePriceComponentRepository.findByMovie(movie);
        for (MoviePriceComponent mpc : prices) {
            price += mpc.getPriceComponent().getPrice();
        }
        return price;
    }
    
    private Integer getScreeningAdditionalPrice(Screening screening) {
        Integer price = 0;
        List<ScreeningPriceComponent> prices = screeningPriceComponentRepository.findByScreening(screening);
        for (ScreeningPriceComponent spc : prices) {
            price += spc.getPriceComponent().getPrice();
        }
        return price;
    }

    @Override
    public void createPriceComponent(String name, Integer price) {
        Optional<PriceComponent> priceComponent = priceRepository.findByName(name);
        if (priceComponent.isPresent()) {
            PriceComponent p = priceComponent.get();
            p.setPrice(price);
            priceRepository.save(p);
        } else {
            PriceComponent p = new PriceComponent(name, price);
            priceRepository.save(p);
        }
    }
    
    @Override
    public void attachPriceComponentToRoom(String priceName, String roomName) {
        Optional<Room> roomOpt = roomRepository.findByName(roomName);
        Optional<PriceComponent> priceComponentOpt = priceRepository.findByName(priceName);
        if (roomOpt.isPresent() && priceComponentOpt.isPresent()) {
            Room room = roomOpt.get();
            PriceComponent priceComponent = priceComponentOpt.get();
            RoomPriceComponent roomPriceComponent = new RoomPriceComponent();
            roomPriceComponent.setPriceComponent(priceComponent);
            roomPriceComponent.setRoom(room);
            roomPriceComponentRepository.save(roomPriceComponent);
        }
    }

    @Override
    public void attachPriceComponentToMovie(String priceName, String movieTitle) {
        Optional<Movie> movieOpt = movieRepository.findByTitle(movieTitle);
        Optional<PriceComponent> priceComponentOpt = priceRepository.findByName(priceName);
        if (movieOpt.isPresent() && priceComponentOpt.isPresent()) {
            Movie movie = movieOpt.get();
            PriceComponent priceComponent = priceComponentOpt.get();
            MoviePriceComponent moviePriceComponent = new MoviePriceComponent();
            moviePriceComponent.setPriceComponent(priceComponent);
            moviePriceComponent.setMovie(movie);
            moviePriceComponentRepository.save(moviePriceComponent);
        }
    }
    
    @Override
    public void attachPriceComponentToScreening(String priceName, String movieTitle,
                                                String roomName, LocalDateTime date) {
        Optional<PriceComponent> priceComponentOpt = priceRepository.findByName(priceName);
        List<Screening> screeningList = 
                screeningRepository.findByMovie_TitleAndRoom_NameAndStartDate(movieTitle, roomName, date);
        if (!screeningList.isEmpty() && priceComponentOpt.isPresent()) {
            Screening screening = screeningList.get(0);
            PriceComponent priceComponent = priceComponentOpt.get();
            ScreeningPriceComponent screeningPriceComponent = new ScreeningPriceComponent();
            screeningPriceComponent.setPriceComponent(priceComponent);
            screeningPriceComponent.setScreening(screening);
            screeningPriceComponentRepository.save(screeningPriceComponent);
        }
    }
    
    
}
