package com.epam.training.ticketservice.core.price.persistence.entity;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class ScreeningPriceComponent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @ManyToOne
    private PriceComponent priceComponent;
    
    @ManyToOne
    private Screening screening;

    public PriceComponent getPriceComponent() {
        return priceComponent;
    }

    public void setPriceComponent(PriceComponent priceComponent) {
        this.priceComponent = priceComponent;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.priceComponent);
        hash = 97 * hash + Objects.hashCode(this.screening);
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
        final ScreeningPriceComponent other = (ScreeningPriceComponent) obj;
        if (!Objects.equals(this.priceComponent, other.priceComponent)) {
            return false;
        }
        if (!Objects.equals(this.screening, other.screening)) {
            return false;
        }
        return true;
    }
    
    
}
