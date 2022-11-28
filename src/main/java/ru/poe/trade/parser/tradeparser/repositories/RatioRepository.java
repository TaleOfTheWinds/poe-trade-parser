package ru.poe.trade.parser.tradeparser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poe.trade.parser.tradeparser.models.Ratio;

@Repository
public interface RatioRepository extends JpaRepository<Ratio, Integer> {
    Ratio findByName(String divine);
}
