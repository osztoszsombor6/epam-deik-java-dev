package com.epam.training.ticketservice.core.booking.persistence.entity;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @ManyToOne
    private User user;
    
    @ManyToOne
    private Screening screening;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) 
    private List<Seat> seats;
    
    int price;
    
    public Booking(){
    }

    public Booking(User user, Screening screening, List<Seat> seats, int price) {
        this.user = user;
        this.screening = screening;
        this.seats = seats;
        for (Seat seat : this.seats) {
            seat.setBooking(this);
        }
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.user);
        hash = 67 * hash + Objects.hashCode(this.screening);
        hash = 67 * hash + Objects.hashCode(this.seats);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Booking other = (Booking) obj;
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.screening, other.screening)) {
            return false;
        }
        if (!Objects.equals(this.seats, other.seats)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Booking{" + "id=" + id + ", user=" + user + ", screening=" + screening + ", seats=" + seats + '}';
    }
    
}
