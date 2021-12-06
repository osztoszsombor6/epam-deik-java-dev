package com.epam.training.ticketservice.core.price.persistence.entity;

import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class RoomPriceComponent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @ManyToOne
    private PriceComponent priceComponent;
    
    @ManyToOne
    private Room room;

    public PriceComponent getPriceComponent() {
        return priceComponent;
    }

    public void setPriceComponent(PriceComponent priceComponent) {
        this.priceComponent = priceComponent;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.priceComponent);
        hash = 11 * hash + Objects.hashCode(this.room);
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
        final RoomPriceComponent other = (RoomPriceComponent) obj;
        if (!Objects.equals(this.priceComponent, other.priceComponent)) {
            return false;
        }
        if (!Objects.equals(this.room, other.room)) {
            return false;
        }
        return true;
    }
    
    
}
