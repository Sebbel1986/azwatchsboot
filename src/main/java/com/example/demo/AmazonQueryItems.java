package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class AmazonQueryItems {
    List<AmazonQueryItem> items = new ArrayList<>();

    public void add(AmazonQueryItem amazonQueryItem) {
        items.add(amazonQueryItem);
    }

    public int size() {
        return items.size();
    }
}
