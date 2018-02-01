package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AmazonQuery {

    AmazonQueryHelperClass amazonHelper = new AmazonQueryHelperClass();
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    String offerListingRawUrl = "https://www.amazon.de/gp/offer-listing/";

    public AmazonQueryItems query(String searchQuery) {
        try {
            Document doc = Jsoup.connect("https://www.amazon.de/s/ref=nb_sb_noss?__mk_de_DE=ÅMÅŽÕÑ&url=search-alias%3Daps&field-keywords=" + searchQuery).get();
            Elements wholeElements = doc.select("li[id^=result]");

            AmazonQueryItems queryItems = amazonHelper.createQueryItems(wholeElements);
            return queryItems;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getOfferListingUrl(String mainPageUrl) {
        String productId = extractProductId(mainPageUrl);

        if (productId.equals("")) {
            throw new RuntimeException("Konnte aus der URL " + mainPageUrl + " keine ProductId extrahieren");
        }

        return offerListingRawUrl + productId + "/";

    }

    private String getOfferListingUrlById(String id) {
        return offerListingRawUrl + id;
    }

    private String extractProductId(String mainPageUrl) {
        String[] content = mainPageUrl.split("/");

        for (String part : content
                ) {

            if (StringUtil.isNumeric(part)) {
                return part;
            }

        }

        return null;
    }

    public AmazonOfferListing getOfferListingById(String id) {
        String offerListingUrl = getOfferListingUrlById(id);
        return getOfferListings(offerListingUrl);
    }

    public com.example.demo.AmazonOfferListing getOfferListings(String offerListingUrl) {
        AmazonOfferListing amazonOfferListing = new AmazonOfferListing();

        try {
            Document doc = Jsoup.connect(offerListingUrl).get();
            Elements offerListingColumns = doc.select("div.a-row.olpOffer");

            for (int i = 1; i <= offerListingColumns.size(); i++) {
                Element row = offerListingColumns.get(i - 1);
                amazonOfferListing.add(createOfferListingItem(row, i));
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return amazonOfferListing;
    }

    private AmazonOfferListingItem createOfferListingItem(Element row, int index) {
        Element element = row.select("div.a-column.olpPriceColumn").first();
        AmazonOfferListingItem offerListingItem = new AmazonOfferListingItem();

        offerListingItem.setInternalId(index);
        try {
            BigDecimal rawPrice = getRawPrice(row);
            offerListingItem.setRawPrice(rawPrice);
        } catch (ParseException e) {
            log.error("Error extracting raw price.", e);
        }

        try {
            BigDecimal shippmentPrice = getShipmentPrice(row);
            offerListingItem.setShipmentPrice(shippmentPrice);
        } catch (ParseException e) {
            log.error("Error extracting shipment price", e);
        }

        boolean isShippingFromGermany = getIsShippingFromGermany(row);
        offerListingItem.setShipsFromGermany(isShippingFromGermany);

        return offerListingItem;
    }

    private BigDecimal getShipmentPrice(Element row) throws ParseException {
        Elements shippingInfo = row.select("p.olpShippingInfo");

        if (hasShippmentCosts(shippingInfo)) {
            String shippingPriceAsText = shippingInfo.select(".olpShippingPrice").text();
            BigDecimal shippingPrice = AmazonQueryHelperClass.parse(shippingPriceAsText, Locale.GERMAN);
            return shippingPrice;
        } else {
            return new BigDecimal(0);
        }
    }

    private boolean hasShippmentCosts(Elements shippingInfo) {
        return shippingInfo.select("span.olpShippingPrice") != null && !shippingInfo.select("span.olpShippingPrice").text().equals("");
    }

    private BigDecimal getRawPrice(Element rawPriceElement) throws ParseException {
        Element price = rawPriceElement.select("span.olpOfferPrice").first();
        String rawPriceText = price.text();

        BigDecimal rawPrice = AmazonQueryHelperClass.parse(rawPriceText, Locale.GERMAN);

        return rawPrice;
    }

    private boolean getIsShippingFromGermany(Element row) {
        return row.select("div.olpDeliveryColumn").text().contains("Versand aus Deutschland");
    }

    public AmazonProduct getAmazonProductById(String amazonId) {
        AmazonProduct amazonProduct = new AmazonProduct();
        amazonProduct.setAmazonId(amazonId);

        try {
            Document doc = Jsoup.connect("https://www.amazon.de/gp/product/" + amazonId + "/").get();
            amazonProduct.setName(getProductTitle(doc));
            amazonProduct.setAuthor(getAuthor(doc));
            amazonProduct.setFullName(getFullName(doc));
            amazonProduct.setISBN13(getISBN13(doc));
            amazonProduct.setCoverImages(getCoverImage(doc));

            setOfferListing(amazonProduct);
            amazonProduct.setCheapest(getCheapest(amazonProduct));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return amazonProduct;
    }

    private String getProductTitle(Document document) {
        return document.select("#productTitle").text();
    }

    private String getAuthor(Document document) {
        String author = document.select(".contributorNameID").text();
        return author;
    }

    private String getFullName(Document document) {
        return document.select("h1#title").text();
    }

    private String getISBN13(Document document){
        return document.select("td.bucket").select("div.content").select("li:contains(ISBN-10)").first().textNodes().get(0).text().trim();
    }

    private List<String> getCoverImage(Document doc){
        String imageString = doc.select("#imgBlkFront").attr("data-a-dynamic-image").toString();
        return getCoverUrls(imageString);
    }

    public List<String> getCoverUrls(String imageString){
        boolean urlOpend = false;
        String url = "";

        List<String> urls = new ArrayList<>();

        for(int i = 0; i < imageString.length(); i++){
            char element = imageString.charAt(i);

            if(element == '\"' && urlOpend == false){
                urlOpend = true;
                continue;
            }

            if(element != '\"' && urlOpend){
                url += element;
                continue;
            }

            if(element == '\"' && urlOpend){
                urlOpend = false;
                urls.add(url);
                url = "";
                continue;
            }
        }

        return urls;
    }

    private void setOfferListing(AmazonProduct amazonProduct) {
        AmazonOfferListing amazonOfferListing = getOfferListingById(amazonProduct.getAmazonId());
        amazonProduct.setOfferListing(amazonOfferListing);
    }

    private AmazonOfferListingItem getCheapest(AmazonProduct amazonProduct) {
        if (amazonProduct.getOfferListing().size() > 0) {
            return amazonProduct.getOfferListing().getItems().get(0);
        }

        return null;
    }
}
