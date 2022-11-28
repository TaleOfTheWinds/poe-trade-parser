package ru.poe.trade.parser.tradeparser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poe.trade.parser.tradeparser.models.Blacklist;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Integer> {
}
