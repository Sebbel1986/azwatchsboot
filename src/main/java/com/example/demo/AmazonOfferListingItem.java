package com.example.demo;

import java.math.BigDecimal;

public class AmazonOfferListingItem implements Comparable<AmazonOfferListingItem>{
    private BigDecimal rawPrice;
    private BigDecimal shipmentPrice;
    private boolean shipsFromGermany;
    private int internalId;

    public void setRawPrice(BigDecimal rawPrice) {
        this.rawPrice = rawPrice;
    }

    public void setShipmentPrice(BigDecimal shipmentPrice) {
        this.shipmentPrice = shipmentPrice;
    }

    public BigDecimal getTotal(){
        return rawPrice.add(shipmentPrice);
    }

    @Override
    public int compareTo(AmazonOfferListingItem compareObject) {
        BigDecimal result = this.getTotal().subtract(compareObject.getTotal());
        if(result.doubleValue() > 0){
            return 1;
        }
        return -1;
    }

    public void setInternalId(int internalId){
        this.internalId = internalId;
    }

    public int getInternalId() {
        return internalId;
    }

    public void setShipsFromGermany(boolean shipsFromGermany){
        this.shipsFromGermany = shipsFromGermany;
    }

    public boolean isShippingFromGermany(){
        return this.shipsFromGermany;
    }
}
