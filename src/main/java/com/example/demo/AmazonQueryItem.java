package com.example.demo;

import java.math.BigDecimal;

public class AmazonQueryItem {
    private String title;
    private BigDecimal otherOfferPrice;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setOtherOfferPrice(BigDecimal otherOfferPrice) {
        this.otherOfferPrice = otherOfferPrice;
    }

    public BigDecimal getOtherOfferPrice() {
        return this.otherOfferPrice;
    }
}
