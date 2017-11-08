package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class JsoupLearningTests {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void getAmazonSearchPageTitles(){
        try {
            Document doc = Jsoup.connect("https://www.amazon.de/s/ref=nb_sb_noss?__mk_de_DE=ÅMÅŽÕÑ&url=search-alias%3Daps&field-keywords=das+verborgene+wort").get();
            Elements titles = doc.select("h2.s-access-title");
            Assert.assertTrue(titles.size() > 0);
            log.info("Found " + titles.size() + " titles");
        } catch (IOException e) {
            log.error("Error while trying to query website.", e);
            e.printStackTrace();
        }
    }

    @Test
    public void getAmazonSearchPageWholeItems(){
        try {
            Document doc = Jsoup.connect("https://www.amazon.de/s/ref=nb_sb_noss?__mk_de_DE=ÅMÅŽÕÑ&url=search-alias%3Daps&field-keywords=das+verborgene+wort").get();
            Elements wholeElements = doc.select("li[id^=result]");
            Assert.assertTrue(wholeElements.size() > 0);
            log.info("Found " + wholeElements.size() + " whole elements.");
        } catch (IOException e) {
            log.error("Error while trying to query website.", e);
            e.printStackTrace();
        }
    }

    @Test
    public void getCheapestPrice() throws ParseException {
        try {
            Document doc = Jsoup.connect("https://www.amazon.de/s/ref=nb_sb_noss?__mk_de_DE=ÅMÅŽÕÑ&url=search-alias%3Daps&field-keywords=das+verborgene+wort").get();
            Elements wholeElements = doc.select("li[id^=result]");
            Assert.assertTrue(wholeElements.size() > 0);

            Element cheapestPrice = wholeElements.select("a[href^=https://www.amazon.de/gp/offer-listing/]").first().select("span.a-color-price").first();
            Assert.assertNotNull(cheapestPrice.text());

            BigDecimal priceAsDecimal = parse(cheapestPrice.text(), Locale.GERMAN);
            Assert.assertTrue(priceAsDecimal.doubleValue() > 0.0);

            log.info("Found cheapest price of " + priceAsDecimal + " for first element.");
        } catch (IOException e) {
            log.error("Error while trying to query website.", e);
            e.printStackTrace();
        }

    }

    public static BigDecimal parse(final String amount, final Locale locale) throws ParseException {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]",""));
    }
}
