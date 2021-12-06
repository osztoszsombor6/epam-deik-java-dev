/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.core.price.persistence.entity;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author zsombor2
 */
public class ScreeningPriceComponentTest {
    

    /**
     * Test of equals method, of class ScreeningPriceComponent.
     */
    @Test
    public void testEquals() {
        PriceComponent pc1 = new PriceComponent("pc", 100);
        pc1.setId(1);
        PriceComponent pc2 = new PriceComponent("pc", 100);
        pc2.setId(2);
        Room room1 = new Room("room1", 10, 10);
        Room room2 = new Room("room2", 10, 10);
        Movie movie1 = new Movie("movie1", "drama", 100);
        Movie movie2 = new Movie("movie2", "drama", 100);
        Screening screening1 = new Screening(movie1, room1, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        Screening screening2 = new Screening(movie2, room2, LocalDateTime.of(2000, Month.MARCH, 1, 12, 0));
        ScreeningPriceComponent spc1 = new ScreeningPriceComponent();
        spc1.setPriceComponent(pc1);
        spc1.setScreening(screening1);
        ScreeningPriceComponent spc2 = new ScreeningPriceComponent();
        spc2.setPriceComponent(pc1);
        spc2.setScreening(screening1);
        ScreeningPriceComponent spc3 = new ScreeningPriceComponent();
        spc3.setPriceComponent(pc2);
        spc3.setScreening(screening1);
        ScreeningPriceComponent spc4 = new ScreeningPriceComponent();
        spc4.setPriceComponent(pc1);
        spc4.setScreening(screening2);
        
        assertEquals(spc1, spc2);
        assertEquals(spc1, spc1);
        assertNotEquals(spc1, spc3);
        assertNotEquals(spc1, spc4);
        assertNotEquals(spc1, 1);
        assertNotEquals(spc1, null);
    }
    
}
