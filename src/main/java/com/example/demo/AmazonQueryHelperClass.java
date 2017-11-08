package com.example.demo;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class AmazonQueryHelperClass {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AmazonQueryItems createQueryItems(Elements wholeElements) {
        AmazonQueryItems amazonQueryItems = new AmazonQueryItems();

        for(int i = 0; i < wholeElements.size(); i++){
            Element currentElement = wholeElements.get(i);
            amazonQueryItems.add(createAmazonQueryItem(currentElement));
        }

        return amazonQueryItems;
    }

    private AmazonQueryItem createAmazonQueryItem(Element element){
        AmazonQueryItem queryItem = new AmazonQueryItem();
        queryItem.setTitle(getTitle(element));
        try {
            queryItem.setOtherOfferPrice(getOtherOfferPrice(element));
        } catch (ParseException e) {
            log.error("Can't convert price string to bigdecimal value.", e);
        }

        return queryItem;
    }

    private String getTitle(Element element){
        return element.select("h2.s-access-title").first().text();
    }

    private BigDecimal getOtherOfferPrice(Element element) throws ParseException {
        if(hasOtherOffers(element)){
            String cheapestPriceAsText = element.select("a[href^=https://www.amazon.de/gp/offer-listing/]").first().select("span.a-color-price").text();
            return parse(cheapestPriceAsText, Locale.GERMAN);
        }

        return new BigDecimal(0);
    }

    private boolean hasOtherOffers(Element element){
        return element.select("a[href^=https://www.amazon.de/gp/offer-listing/]").first() != null;
    }

    public static BigDecimal parse(final String amount, final Locale locale) throws ParseException {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]",""));
    }
}
