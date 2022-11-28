package ru.poe.trade.parser.tradeparser.util.parser;

import org.json.JSONObject;
import ru.poe.trade.parser.tradeparser.controllers.MainController;
import ru.poe.trade.parser.tradeparser.models.Trade;
import ru.poe.trade.parser.tradeparser.services.TradeService;
import ru.poe.trade.parser.tradeparser.util.IOUtil;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PoeTradeParser extends Thread {
    private final TradeService tradeService;
    public static String SITE_URL = "https://www.pathofexile.com/api/trade/exchange/";
    public final String LEAGUE;
    public static String jsonInputStringDivine = IOUtil.getStringFromFile(new File("C:\\Users\\super\\ScarabParser\\resources\\jsonInputStringDivine"));
    public static String jsonInputStringChaos = IOUtil.getStringFromFile(new File("C:\\Users\\super\\ScarabParser\\resources\\jsonInputStringChaos"));
    public final double DIVINE_RATIO; //scarabs per divine
    public final double CHAOS_RATIO; //chaos per scarab
    public final int MIN_STOCK;
    public static boolean isFound = false;

    public PoeTradeParser(TradeService tradeService, String LEAGUE, double DIVINE_RATIO, double CHAOS_RATIO, int MIN_STOCK) {
        this.tradeService = tradeService;
        this.LEAGUE = LEAGUE;
        this.DIVINE_RATIO = DIVINE_RATIO;
        this.CHAOS_RATIO = CHAOS_RATIO;
        this.MIN_STOCK = MIN_STOCK;
    }

    @Override
    public void run() {
        while (MainController.isStarted) {
            try {
                startProgram();
            } catch (IOException | InterruptedException e) {
                MainController.isStarted = false;
                System.out.println("Program has been stopped");
            }
        }
    }

    public void startProgram() throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        startSearch("c");
        startSearch("d");
        System.out.println(System.currentTimeMillis()-start);
        if (isFound) {
            isFound = false;
            playSound();
        }
        Thread.sleep(297750);
        tradeService.truncate();
        System.out.println(System.currentTimeMillis()-start);
    }

    public void playSound() throws InterruptedException {
        File sound = new File("C:\\Users\\super\\ScarabParser\\resources\\Sound.wav");
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(1000);
    }

    public void startSearch(String currency) throws IOException {
        //get json with items from Poe api
        String json = init(currency);
        //get items list from json
        List<Item> items = parseItems(json);
        List<Item> formattedItems = formatAndAddItems(items);
        //filter items
        if (currency.equals("c")) {
            formattedItems.sort((a, b) -> Double.compare(b.getRatio(), a.getRatio()));
        } else if (currency.equals("d")) {
            formattedItems.sort((a, b) -> Double.compare(a.getRatio(), b.getRatio()));
        }
        //add items to db
        findItems(formattedItems);
    }

    public Item mergeItems(List<Item> items) {
        Item item = new Item();
        item.setItemName("gilded scarab");
        item.setCurrencyName(items.get(0).getCurrencyName());
        item.setAccountName(items.get(0).getAccountName());
        item.setStock(items.get(0).getStock());
        item.setRatio(items.get(0).getRatio());
        for (int i = 1; i < items.size(); i++) {
            Item it = items.get(i);
            item.setRatio((item.getRatio() + it.getRatio()) / 2.0);
            item.setStock(item.getStock() + it.getStock());
        }
        return item;
    }

    //ищет айтемы с одинаковыми продавцами
    public List<Item> formatAndAddItems(List<Item> items) {
        Map<String, List<Item>> itemsForMerge = new HashMap<>();
        for (Item item : items) {
            if (itemsForMerge.containsKey(item.getAccountName())) {
                List<Item> items1 = itemsForMerge.get(item.getAccountName());
                items1.add(item);
                itemsForMerge.put(item.getAccountName(), items1);
            } else itemsForMerge.put(item.getAccountName(), new ArrayList<>(List.of(item)));
        }

        List<Item> result = new ArrayList<>();

        for (List<Item> list : itemsForMerge.values()) {
            if (list.size() == 1) {
                result.add(list.get(0));
            } else {
                result.add(mergeItems(list));
            }
        }

        return result;
    }

    public void log(String json) {
        try (PrintWriter writer = new PrintWriter("src/main/resources/static/jsonOutputString")) {
            writer.write(new JSONObject(json).toString(4));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void findItems(List<Item> items) {
        for (Item item : items) {
            if (item.getStock() >= MIN_STOCK) {
                tradeService.save(new Trade(item.getRatio(), item.getItemName(), item.getStock(), item.getAccountName(), item.getCurrencyName()));
                isFound = true;
            }
        }
    }

    public List<Item> parseItems(String json) {
        JSONObject object = new JSONObject(json);
        JSONObject results = object.getJSONObject("result");
        Map<String, Object> stringObjectMap = results.toMap();

        List<Item> items = new ArrayList<>();
        for (Object obj : stringObjectMap.values()) {
            Item finalItem = new Item();

            HashMap map = (HashMap) obj;
            HashMap listing = (HashMap) map.get("listing");
            List offers = (List) listing.get("offers");
            HashMap o = (HashMap) offers.get(0);
            HashMap item = (HashMap) o.get("item");
            HashMap exchange = (HashMap) o.get("exchange");
            int itemAmountPer = (int) item.get("amount");
            String whisper = (String) item.get("whisper");
            String itemName = (String) item.get("currency");
            String id = (String) item.get("id");
            int stock = (int) item.get("stock");

            int currencyAmountPer = (int) exchange.get("amount");
            String whisperE = (String) exchange.get("whisper");
            String currency = (String) exchange.get("currency");

            HashMap account = (HashMap) listing.get("account");
            String name = (String) account.get("name");

            finalItem.setAccountName(name);
            finalItem.setStock(stock);
            finalItem.setCurrencyName(currency);
            finalItem.setItemName(itemName);

            ratioCalc(finalItem, itemAmountPer, currencyAmountPer);
            if (finalItem.getCurrencyName().equals("divine")) {
                if (finalItem.getRatio() >= DIVINE_RATIO)
                    items.add(finalItem);
            } else if (finalItem.getCurrencyName().equals("chaos")) {
                if (finalItem.getRatio() <= CHAOS_RATIO)
                    items.add(finalItem);
            } else throw new RuntimeException("Unknown currency");
        }
        return items;
    }

    public void ratioCalc(Item item, int itemAmountPerTrade, int currencyAmountPerTrade) {
        if (item.getCurrencyName().equals("chaos")) {
            item.setRatio((currencyAmountPerTrade * 1.0) / itemAmountPerTrade);
        } else if (item.getCurrencyName().equals("divine")) {
            item.setRatio((itemAmountPerTrade * 1.0) / currencyAmountPerTrade);
        }
    }

    public String init(String currency) throws IOException {
        URL url = new URL(SITE_URL+LEAGUE);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.124 YaBrowser/22.9.3.886 Yowser/2.5 Safari/537.36");
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = null;
            if (currency.equals("c")) {
                input = jsonInputStringChaos.getBytes(StandardCharsets.UTF_8);
            } else input = jsonInputStringDivine.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        String result = "";
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            result = response.toString();
        }
        return result;
    }
}
