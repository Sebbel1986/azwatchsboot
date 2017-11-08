package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AmazonOfferListing {
    List<AmazonOfferListingItem> items = new ArrayList<>();

    public int size() {
        return items.size();
    }

    public AmazonOfferListingItem getCheapest() {
        if(items.size() > 0){
            Collections.sort(items);
            return items.get(0);
        }

        return null;
    }

    public void add(AmazonOfferListingItem offerListingItem) {
        items.add(offerListingItem);
    }

    public List<AmazonOfferListingItem> getItems() {
        return items;
    }
}
