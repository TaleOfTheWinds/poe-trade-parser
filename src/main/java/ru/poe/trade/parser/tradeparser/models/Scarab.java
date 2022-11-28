package ru.poe.trade.parser.tradeparser.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "scarab")
public class Scarab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private Double value;

    public Scarab() {
    }

    public Scarab(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " : " + value;
    }
}
