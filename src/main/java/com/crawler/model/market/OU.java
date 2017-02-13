package com.crawler.model.market;

import com.crawler.model.Ratio;
import org.jsoup.nodes.Element;

import java.util.NoSuchElementException;

public class OU implements Market {
    private final Type type;
    private final String selector;
    private final String descStartWith;

    public OU() {
        this.selector = "td";
        this.type = Type.OU;
        this.descStartWith = "Тотал голов в матче больше";
    }

    @Override
    public Ratio getRatio(Element element) {
        String betVariation;
        Double value;
        Double ratio = Double.valueOf(element.select(".eventprice").text());
        String desc = element.select(".eventselection").text();

        if 		(desc.toLowerCase().contains("больше"))	betVariation = "TU";
        else if (desc.toLowerCase().contains("меньше"))	betVariation = "TO";
        else 	throw new NoSuchElementException();

        value = Double.valueOf(desc.replaceAll("[^0-9.,]+",""));

        return new Ratio(betVariation, value, ratio, type);
    }

    @Override
    public String getSelector() {
        return selector;
    }

    @Override
    public String getDescStartWith() {
        return descStartWith;
    }
}
