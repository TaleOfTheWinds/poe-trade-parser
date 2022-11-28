package ru.poe.trade.parser.tradeparser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.poe.trade.parser.tradeparser.models.Trade;

@ResponseBody
public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
