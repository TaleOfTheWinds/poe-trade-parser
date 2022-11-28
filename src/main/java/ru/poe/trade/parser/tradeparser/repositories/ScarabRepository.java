package ru.poe.trade.parser.tradeparser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poe.trade.parser.tradeparser.models.Scarab;

@Repository
public interface ScarabRepository extends JpaRepository<Scarab, Integer> {
    Scarab findByName(String name);
}
