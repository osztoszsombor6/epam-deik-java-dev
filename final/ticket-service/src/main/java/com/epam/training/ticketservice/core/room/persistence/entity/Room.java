package com.epam.training.ticketservice.core.room.persistence.entity;

import com.epam.training.ticketservice.core.price.persistence.entity.PriceComponent;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String name;
    private Integer rowNum;
    private Integer colNum;
    

    public Room() {
    }

    public Room(String name, Integer rowNum, Integer colNum) {
        this.name = name;
        this.rowNum = rowNum;
        this.colNum = colNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getColNum() {
        return colNum;
    }

    public void setColNum(Integer colNum) {
        this.colNum = colNum;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + Objects.hashCode(this.rowNum);
        hash = 79 * hash + Objects.hashCode(this.colNum);
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
        final Room other = (Room) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
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
        return "Room{" + "id=" + id + ", name=" + name + ", rowNum=" + rowNum + ", colNum=" + colNum + '}';
    }

    
}
