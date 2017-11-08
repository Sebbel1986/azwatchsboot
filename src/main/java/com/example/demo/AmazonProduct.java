package com.example.demo;

import sun.font.AttributeValues;

public class AmazonProduct {
    private String name;
    private String amazonId;
    private AmazonOfferListing offerListing;
    private String author;
    private String fullName;
    private AmazonOfferListingItem cheapest;
    private String ISBN13;



    public String getAmazonId() {
        return this.amazonId;
    }

    public String getName() {
        return this.name;
    }

    public String getFullName(){
        return this.fullName;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN13() {
        return ISBN13;
    }

    public AmazonOfferListingItem getCheapest(){
        return this.cheapest;
    }

    public AmazonOfferListing getOfferListing() {
        return offerListing;
    }

    public void setAmazonId(String amazonId) {
        this.amazonId = amazonId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOfferListing(AmazonOfferListing offerListing) {
        this.offerListing = offerListing;
    }

    public void setAuthor(String author) {
        this.author = author;
    }



    public void setFullName(String format) {
        this.fullName = format;
    }



    public void setCheapest(AmazonOfferListingItem cheapest){
        this.cheapest = cheapest;
    }





    public void setISBN13(String ISBN13) {
        this.ISBN13 = ISBN13;
    }
}
