package com.example.demo;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class AmazonQueryTests {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testGetSearchPage() {
        AmazonQuery amazonQuery = new AmazonQuery();
        AmazonQueryItems items = amazonQuery.query("Das+verborgene+Wort");

        Assert.assertTrue(items.size() > 0);
        log.info("Found a total of " + items.size() + " items");
    }

    @Test
    public void testGetOfferListingUrlByMainUrl() {
        String queryUrlDasVerborgeneWort = "https://www.amazon.de/dp/3423210559/";

        AmazonQuery amazonQuery = new AmazonQuery();
        String offerListingUrl = amazonQuery.getOfferListingUrl(queryUrlDasVerborgeneWort);

        Assert.assertTrue(offerListingUrl.equals("https://www.amazon.de/gp/offer-listing/3423210559/"));
    }

    @Test
    public void testGetOfferListingColumns() {
        AmazonQuery amazonQuery = new AmazonQuery();

        String offerListingUrl = amazonQuery.getOfferListingUrl("https://www.amazon.de/dp/3423210559/");
        AmazonOfferListing offerListing = amazonQuery.getOfferListings(offerListingUrl);

        Assert.assertTrue(offerListing.size() > 0);

        AmazonOfferListingItem cheapest = offerListing.getCheapest();

        for (AmazonOfferListingItem item : offerListing.getItems()) {
            if (item.getInternalId() != cheapest.getInternalId()) {
                Assert.assertTrue(item.getTotal().doubleValue() > cheapest.getTotal().doubleValue());
            }
        }
        Assert.assertNotNull(offerListing.getCheapest());
    }

    @Test
    public void getOfferListingById() {
        AmazonQuery amazonQuery = new AmazonQuery();

        AmazonOfferListing offerListing = amazonQuery.getOfferListingById("3423210559");

        AmazonOfferListingItem cheapest = offerListing.getCheapest();

        for (AmazonOfferListingItem item : offerListing.getItems()) {
            if (item.getInternalId() != cheapest.getInternalId()) {
                Assert.assertTrue(item.getTotal().doubleValue() > cheapest.getTotal().doubleValue());
            }
        }
        Assert.assertNotNull(offerListing.getCheapest());
    }

    @Test
    public void getAmazonItemById() {
        AmazonQuery amazonQuery = new AmazonQuery();

        AmazonProduct amazonProduct = amazonQuery.getAmazonProductById("3423210559");

        Assert.assertNotNull(amazonProduct);
        Assert.assertTrue(!amazonProduct.getName().equals(""));
        Assert.assertTrue(!amazonProduct.getAuthor().equals(""));
        Assert.assertTrue(amazonProduct.getAmazonId().equals("3423210559"));
        Assert.assertTrue(amazonProduct.getOfferListing().getItems().size() > 0);
        Assert.assertTrue(!amazonProduct.getISBN13().equals(""));
    }
}
