package ru.poe.trade.parser.tradeparser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.poe.trade.parser.tradeparser.models.Blacklist;
import ru.poe.trade.parser.tradeparser.models.Trade;
import ru.poe.trade.parser.tradeparser.repositories.TradeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TradeService {
    private final TradeRepository repository;
    private final BlacklistService blacklistService;

    @Autowired
    public TradeService(TradeRepository repository, BlacklistService blacklistService) {
        this.repository = repository;
        this.blacklistService = blacklistService;
    }

    public List<Trade> findAll() {
        List<Blacklist> blacklist = blacklistService.findAll();
        List<Trade> trades = repository.findAll();
        for (Blacklist b : blacklist) {
            trades.removeIf(trade -> trade.getAccountName().equals(b.getAccountName()));
        }
        return trades;
    }

    @Transactional
    public void save(Trade trade) {
        repository.save(trade);
    }

    @Transactional
    public void truncate() {
        repository.deleteAll();
    }
}
