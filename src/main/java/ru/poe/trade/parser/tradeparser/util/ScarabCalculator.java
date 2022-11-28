package ru.poe.trade.parser.tradeparser.util;

import ru.poe.trade.parser.tradeparser.models.Scarab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ScarabCalculator {
    private int currentIndex = 14; //shaper default
    private List<Scarab> scarabs;
    private ThreadLocalRandom randomLocal = ThreadLocalRandom.current();
    private int count = 0;
    private final double rerollCost;
    private final double scarabCost;
    private final double divine_ratio;

    public ScarabCalculator(List<Scarab> scarabs) {
        rerollCost = (30.0/scarabs.get(16).getValue())*10.0;
        scarabCost = scarabs.get(17).getValue()*10.0;
        divine_ratio = scarabs.get(18).getValue();
        scarabs.remove(18);
        scarabs.remove(17);
        scarabs.remove(16);
        this.scarabs = scarabs;
    }

    public Double calculate() {
        double result = 0.0;
        double currentResult = 0.0;

        for (int i = 0; i < 1000; i++) { //100_000 total rolls for good average
            while (count < 100) { //1000 total scarabs
                int random = num();
                while (random == currentIndex)
                    random = num();

                currentIndex = random;
                if (scarabs.get(currentIndex).getValue() == 0.0) {
                    currentResult -= rerollCost;
                } else {
                    currentResult += scarabs.get(currentIndex).getValue() * 10.0 - rerollCost - scarabCost;
                    count++;
                }
            }
            count = 0;
            result += currentResult;
            currentResult = 0.0;
        }

        return result/divine_ratio/1000;
    }

    public int num() {
        return randomLocal.nextInt(0, 16);
    }
}
