package com.epam.training.ticketservice.core.booking.persistence.entity;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Seat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @ManyToOne
    private Booking booking;
    
    @Column(name = "row_num")
    private Integer row;
    private Integer col;

    public Seat(){
    }

    public Seat(Booking booking, Integer row, Integer col) {
        this.booking = booking;
        this.row = row;
        this.col = col;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        Screening thisScreening = this.booking != null ? this.booking.getScreening() : null;
        hash = 37 * hash + Objects.hashCode(thisScreening);
        hash = 37 * hash + Objects.hashCode(this.row);
        hash = 37 * hash + Objects.hashCode(this.col);
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
        final Seat other = (Seat) obj;

        Screening otherScreening = other.booking != null ? other.booking.getScreening() : null;
        Screening thisScreening = this.booking != null ? this.booking.getScreening() : null;
        
        if (!Objects.equals(thisScreening, otherScreening)) {
            return false;
        }
        if (!Objects.equals(this.row, other.row)) {
            return false;
        }
        if (!Objects.equals(this.col, other.col)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Seat{" + "id=" + id + ", row=" + row + ", col=" + col + '}';
    }
    
    
}
