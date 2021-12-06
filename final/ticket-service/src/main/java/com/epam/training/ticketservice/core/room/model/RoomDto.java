package com.epam.training.ticketservice.core.room.model;

import java.util.Objects;

public class RoomDto {

    private final String name;
    private final Integer rowNum;
    private final Integer colNum;

    public RoomDto(String name, Integer rowNum, Integer colNum) {
        this.name = name;
        this.rowNum = rowNum;
        this.colNum = colNum;
    }


    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public Integer getColNum() {
        return colNum;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.name);
        hash = 17 * hash + Objects.hashCode(this.rowNum);
        hash = 17 * hash + Objects.hashCode(this.colNum);
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
        final RoomDto other = (RoomDto) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.rowNum, other.rowNum)) {
            return false;
        }
        if (!Objects.equals(this.colNum, other.colNum)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RoomDto{" + "name=" + name + ", rowNum=" + rowNum + ", colNum=" + colNum + '}';
    }



    public static class Builder {
        private String name;
        private Integer rowNum;
        private Integer colNum;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withRowNum(Integer rowNum) {
            this.rowNum = rowNum;
            return this;
        }
        
        public Builder withColNum(Integer colNum) {
            this.colNum = colNum;
            return this;
        }

        public RoomDto build() {
            return new RoomDto(name, rowNum, colNum);
        }
    }
}