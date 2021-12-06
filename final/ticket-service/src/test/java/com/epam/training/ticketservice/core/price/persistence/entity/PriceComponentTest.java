/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.core.price.persistence.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author zsombor2
 */
public class PriceComponentTest {
    
    


    /**
     * Test of equals method, of class PriceComponent.
     */
    @Test
    public void testEquals() {
        PriceComponent pc1 = new PriceComponent("pc", 100);
        pc1.setId(1);
        PriceComponent pc2 = new PriceComponent("pc", 100);
        pc2.setId(1);
        PriceComponent pc3 = new PriceComponent("pc", 100);
        pc3.setId(2);
        PriceComponent pc4 = new PriceComponent("p", 100);
        pc4.setId(1);
        PriceComponent pc5 = new PriceComponent("pc", 10);
        pc5.setId(1);
        
        assertEquals(pc1, pc2);
        assertEquals(pc1, pc1);
        assertNotEquals(pc1, pc3);
        assertNotEquals(pc1, pc4);
        assertNotEquals(pc1, pc5);
        assertNotEquals(pc1, 1);
        assertNotEquals(pc1, null);
    }
    
}
