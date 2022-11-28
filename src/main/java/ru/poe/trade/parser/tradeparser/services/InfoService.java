package ru.poe.trade.parser.tradeparser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.poe.trade.parser.tradeparser.models.Info;
import ru.poe.trade.parser.tradeparser.repositories.InfoRepository;

@Service
@Transactional(readOnly = true)
public class InfoService {
    private final InfoRepository infoRepository;

    @Autowired
    public InfoService(InfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    public Info findByName(String name) {
        return infoRepository.findByName(name);
    }

    @Transactional
    public void update(String leagueToSave, Integer minStock) {
        Info league = infoRepository.findByName("league");
        Info stock = infoRepository.findByName("stock");
        league.setValue(leagueToSave);
        stock.setValue(minStock.toString());
    }
}
