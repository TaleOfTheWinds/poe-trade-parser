package ru.poe.trade.parser.tradeparser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.poe.trade.parser.tradeparser.models.Blacklist;
import ru.poe.trade.parser.tradeparser.repositories.BlacklistRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BlacklistService {
    private final BlacklistRepository repository;

    @Autowired
    public BlacklistService(BlacklistRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveByName(String accountName) {
        repository.save(new Blacklist(accountName));
    }

    public List<Blacklist> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void truncate() {
        repository.deleteAll();
    }
}
