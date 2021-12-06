package com.epam.training.ticketservice.core.booking.model;

public class SeatDto {
    
    private final Integer row;
    private final Integer col;

    public SeatDto(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public Integer getRow() {
        return row;
    }

    public Integer getCol() {
        return col;
    }
    
    public static class Builder {
        private Integer row;
        private Integer col;

        public Builder withRow(Integer row) {
            this.row = row;
            return this;
        }

        public Builder withCol(Integer col) {
            this.col = col;
            return this;
        }
        

        public SeatDto build() {
            return new SeatDto(row, col);
        }
    }
    
}
