package ru.poe.trade.parser.tradeparser.util;

import ru.poe.trade.parser.tradeparser.models.Scarab;

import java.util.List;

public class ScarabWrapper {
    private List<Scarab> scarabs;

    public ScarabWrapper() {
    }

    public ScarabWrapper(List<Scarab> scarabs) {
        this.scarabs = scarabs;
    }

    public List<Scarab> getScarabs() {
        return scarabs;
    }

    public void setScarabs(List<Scarab> scarabs) {
        this.scarabs = scarabs;
    }
}
