/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.core.price.persistence.entity;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author zsombor2
 */
public class MoviePriceComponentTest {
    

    /**
     * Test of equals method, of class MoviePriceComponent.
     */
    @Test
    public void testEquals() {
        PriceComponent pc1 = new PriceComponent("pc", 100);
        pc1.setId(1);
        PriceComponent pc2 = new PriceComponent("pc", 100);
        pc2.setId(2);
        Movie movie1 = new Movie("movie1", "drama", 100);
        Movie movie2 = new Movie("movie2", "drama", 100);
        MoviePriceComponent mpc1 = new MoviePriceComponent();
        mpc1.setPriceComponent(pc1);
        mpc1.setMovie(movie1);
        MoviePriceComponent mpc2 = new MoviePriceComponent();
        mpc2.setPriceComponent(pc1);
        mpc2.setMovie(movie1);
        MoviePriceComponent mpc3 = new MoviePriceComponent();
        mpc3.setPriceComponent(pc2);
        mpc3.setMovie(movie1);
        MoviePriceComponent mpc4 = new MoviePriceComponent();
        mpc4.setPriceComponent(pc1);
        mpc4.setMovie(movie2);
        
        assertEquals(mpc1, mpc2);
        assertEquals(mpc1, mpc1);
        assertNotEquals(mpc1, mpc3);
        assertNotEquals(mpc1, mpc4);
        assertNotEquals(mpc1, 1);
        assertNotEquals(mpc1, null);
    }
    
}
