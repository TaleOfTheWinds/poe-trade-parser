package ru.poe.trade.parser.tradeparser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.poe.trade.parser.tradeparser.models.Scarab;
import ru.poe.trade.parser.tradeparser.repositories.ScarabRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ScarabService {
    private final ScarabRepository repository;

    @Autowired
    public ScarabService(ScarabRepository repository) {
        this.repository = repository;
    }

    public List<Scarab> findAll() {
        return repository.findAll();
    }

    public Scarab findByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    public void saveAll(List<Scarab> scarabs) {
        for (Scarab scarab : scarabs) {
            Scarab s = findByName(scarab.getName());
            s.setValue(scarab.getValue());
        }
    }
}
