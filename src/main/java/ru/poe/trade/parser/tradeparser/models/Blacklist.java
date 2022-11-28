package ru.poe.trade.parser.tradeparser.models;

import javax.persistence.*;

@Entity
@Table(name = "blacklist")
public class Blacklist {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "account_name")
    private String accountName;

    public Blacklist() {
    }

    public Blacklist(String accountName) {
        this.accountName = accountName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
