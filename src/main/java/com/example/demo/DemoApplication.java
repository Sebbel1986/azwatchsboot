package com.example.demo;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}



}

@RestController
class HelloWorld {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	ResponseEntity<String> helloWorld() {

		try {
			Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
		} catch (IOException e) {
			log.error("Error while trying to query website.", e);
			e.printStackTrace();
		}
		return new ResponseEntity<String>("Hello World!", HttpStatus.OK);
	}
}

@RestController
class AmazonController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/amazon/all/{id}", method = RequestMethod.GET, produces = "application/json")
	ArrayList<AmazonOfferListingItem> getAll(@PathVariable String id) {
		try {
			AmazonQuery amazonQuery = new AmazonQuery();
			return (ArrayList<AmazonOfferListingItem>) amazonQuery.getOfferListingById(id).getItems();
		} catch (Exception e){
			log.error("Can't fetch amazon offer listing page.", e);
		}

		return new ArrayList<>();
	}

	@RequestMapping(value = "/amazon/cheapest/{id}", method = RequestMethod.GET, produces = "application/json")
	AmazonOfferListingItem getCheapest(@PathVariable String id) {
		try {
			AmazonQuery amazonQuery = new AmazonQuery();
			ArrayList<AmazonOfferListingItem> result = (ArrayList<AmazonOfferListingItem>) amazonQuery.getOfferListingById(id).getItems();
			if(result.size() > 0){
				return result.get(0);
			} else return null;
		} catch (Exception e){
			log.error("Can't fetch amazon offer listing page.", e);
		}

		return null;
	}

	@RequestMapping(value = "/amazon/product/{id}", method = RequestMethod.GET, produces = "application/json")
	AmazonProduct getProduct(@PathVariable String id) {
		try {
			AmazonQuery amazonQuery = new AmazonQuery();
			AmazonProduct amazonProduct = amazonQuery.getAmazonProductById(id);

			return amazonProduct;
		} catch (Exception e){
			log.error("Can't fetch amazon product.", e);
		}

		return null;
	}
}