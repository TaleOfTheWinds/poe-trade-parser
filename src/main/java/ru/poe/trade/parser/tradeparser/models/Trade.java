package ru.poe.trade.parser.tradeparser.models;

import javax.persistence.*;

@Entity
@Table(name = "Trade")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ratio")
    private Double ratio;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "currency_name")
    private String currencyName;

    public Trade() {
    }

    public Trade(Double ratio, String itemName, Integer stock, String accountName, String currencyName) {
        this.ratio = ratio;
        this.itemName = itemName;
        this.stock = stock;
        this.accountName = accountName;
        this.currencyName = currencyName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
