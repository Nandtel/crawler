package com.crawler.model.market;

import com.crawler.model.Ratio;
import org.jsoup.nodes.Element;

import java.util.NoSuchElementException;

public class OU implements Market {
    final Type type;
    final String selector;

    public OU(String selector) {
        this.selector = selector;

        this.type = Type.OU;
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
}
