/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.training.ticketservice.core.price.persistence.entity;

import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author zsombor2
 */
public class RoomPriceComponentTest {
    

    /**
     * Test of equals method, of class RoomPriceComponent.
     */
    @Test
    public void testEquals() {
        PriceComponent pc1 = new PriceComponent("pc", 100);
        pc1.setId(1);
        PriceComponent pc2 = new PriceComponent("pc", 100);
        pc2.setId(2);
        Room room1 = new Room("room1", 10, 10);
        Room room2 = new Room("room2", 10, 10);
        RoomPriceComponent rpc1 = new RoomPriceComponent();
        rpc1.setPriceComponent(pc1);
        rpc1.setRoom(room1);
        RoomPriceComponent rpc2 = new RoomPriceComponent();
        rpc2.setPriceComponent(pc1);
        rpc2.setRoom(room1);
        RoomPriceComponent rpc3 = new RoomPriceComponent();
        rpc3.setPriceComponent(pc2);
        rpc3.setRoom(room1);
        RoomPriceComponent rpc4 = new RoomPriceComponent();
        rpc4.setPriceComponent(pc1);
        rpc4.setRoom(room2);
        
        assertEquals(rpc1, rpc2);
        assertEquals(rpc1, rpc1);
        assertNotEquals(rpc1, rpc3);
        assertNotEquals(rpc1, rpc4);
        assertNotEquals(rpc1, 1);
        assertNotEquals(rpc1, null);
    }
    
}
