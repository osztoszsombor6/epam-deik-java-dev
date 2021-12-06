package com.epam.training.ticketservice.core.booking.model;


public class InvalidSeatException extends Exception {
    private final Integer row;
    private final Integer col;

    public InvalidSeatException(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getCol() {
        return col;
    }
    
}
