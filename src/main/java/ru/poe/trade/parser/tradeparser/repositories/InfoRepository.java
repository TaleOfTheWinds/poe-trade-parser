package ru.poe.trade.parser.tradeparser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poe.trade.parser.tradeparser.models.Info;

@Repository
public interface InfoRepository extends JpaRepository<Info, Integer> {
    Info findByName(String name);
}
