package com.example.calculator.model;

import jakarta.persistence.*;

@Entity
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String currencyPair;

    @Column(nullable = false)
    private double rate;

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCurrencyPair() { return currencyPair; }
    public void setCurrencyPair(String currencyPair) { this.currencyPair = currencyPair; }
    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }
}