package com.BankingLibrary;


public class Card {

    private String cardNumber;
    private String cvv;
    private String expirationDate;
    private float sold;

    public Card(String cardNumber, String cvv, String expirationDate, float sold) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expirationDate = expirationDate;
        this.sold = sold;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public float getSold() {return sold;}

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setSold(float sold) {
        this.sold = sold;
    }
}
