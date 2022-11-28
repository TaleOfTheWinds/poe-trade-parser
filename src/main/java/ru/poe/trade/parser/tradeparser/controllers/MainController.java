package ru.poe.trade.parser.tradeparser.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.poe.trade.parser.tradeparser.models.Scarab;
import ru.poe.trade.parser.tradeparser.models.Trade;
import ru.poe.trade.parser.tradeparser.util.parser.PoeTradeParser;
import ru.poe.trade.parser.tradeparser.services.*;
import ru.poe.trade.parser.tradeparser.util.ScarabCalculator;
import ru.poe.trade.parser.tradeparser.util.ScarabWrapper;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    public static boolean isStarted = false;
    public static PoeTradeParser currentParser;
    private final TradeService service;
    private final RatioService ratioService;
    private final InfoService infoService;
    private final BlacklistService blacklistService;
    private final ScarabService scarabService;

    @Autowired
    public MainController(TradeService service, RatioService ratioService, InfoService infoService, BlacklistService blacklistService, ScarabService scarabService) {
        this.service = service;
        this.ratioService = ratioService;
        this.infoService = infoService;
        this.blacklistService = blacklistService;
        this.scarabService = scarabService;
    }


    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute(checkProgram(), "");
        model.addAttribute("chaos", ratioService.findRatioByName("chaos"));
        model.addAttribute("divine", ratioService.findRatioByName("divine"));
        model.addAttribute("stock", Integer.parseInt(infoService.findByName("stock").getValue()));
        model.addAttribute("league", infoService.findByName("league").getValue());

        return "mainPage";
    }

    @GetMapping("/info")
    public String infoPage(Model model) {
        List<Trade> items = service.findAll();
        model.addAttribute("items", items);

        return "infoPage";
    }

    @GetMapping("/calculator")
    public String scarabCalculator(Model model) {
        List<Scarab> scarabs = scarabService.findAll();
        model.addAttribute("scarabsList", new ScarabWrapper(scarabs));
        model.addAttribute("result", String.format("%.2f", new ScarabCalculator(new ArrayList<>(scarabs)).calculate()));
        return "calculator";
    }

    @PostMapping("/calculator")
    public String scarabCalculator(@ModelAttribute("scarabsList") ScarabWrapper scarabsList) {
        scarabService.saveAll(scarabsList.getScarabs());
        return "redirect:/calculator";
    }

    @PostMapping("/start")
    public String startProgram(@RequestParam("divineRatio") double divineRatio, @RequestParam("chaosRatio") double chaosRatio,
            @RequestParam("league") String league, @RequestParam("minStock") Integer minStock,
                               Model model) throws InterruptedException {
        //update values and init parser
        ratioService.update(divineRatio, chaosRatio);
        infoService.update(league, minStock);
        service.truncate();
        PoeTradeParser parser = new PoeTradeParser(service, league, divineRatio, chaosRatio, minStock);
        parser.start();
        currentParser = parser;
        isStarted = true;

        model.addAttribute(checkProgram(), "");
        Thread.sleep(1250);
        return "redirect:/info";
    }

    @PostMapping("/stop")
    public String stopProgram(Model model) {
        service.truncate();
        currentParser.interrupt();
        isStarted = false;
        model.addAttribute(checkProgram(), "");
        return "redirect:/";
    }

    @PostMapping("/blacklist")
    public String blacklist(@RequestParam("accountName") String accountName) {
        blacklistService.saveByName(accountName);
        return "redirect:/info";
    }

    @DeleteMapping("/blacklist")
    public String clearBlacklist() {
        blacklistService.truncate();
        return "redirect:/";
    }

    private String checkProgram() {
        if (isStarted == false)
            return "notStarted";
        else return "started";
    }
}
