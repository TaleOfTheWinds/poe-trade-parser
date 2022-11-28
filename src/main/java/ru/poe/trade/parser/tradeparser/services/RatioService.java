package ru.poe.trade.parser.tradeparser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.poe.trade.parser.tradeparser.models.Ratio;
import ru.poe.trade.parser.tradeparser.repositories.RatioRepository;

@Service
@Transactional(readOnly = true)
public class RatioService {
    private final RatioRepository repository;

    @Autowired
    public RatioService(RatioRepository repository) {
        this.repository = repository;
    }

    public double findRatioByName(String name) {
        return repository.findByName(name).getRatio();
    }

    @Transactional
    public void update(double divineRatio, double chaosRatio) {
        Ratio chaos = repository.findByName("chaos");
        Ratio divine = repository.findByName("divine");
        chaos.setRatio(chaosRatio);
        divine.setRatio(divineRatio);
    }
}
