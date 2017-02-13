package com.crawler.model.market;

import com.crawler.model.Ratio;
import org.jsoup.nodes.Element;

import java.util.NoSuchElementException;

public class BTS implements Market {
    private final Type type;
    private final String selector;

    public BTS(String selector) {
        this.selector = selector;

        this.type = Type.BTS;
    }

    @Override
    public Ratio getRatio(Element element) {
        String betVariation;
        Double ratio = Double.valueOf(element.select(".eventprice").text());
        String desc = element.select(".eventselection").text();

        if 		("Да".equalsIgnoreCase(desc))		betVariation = "Y";
        else if ("Нет".equalsIgnoreCase(desc))	betVariation = "N";
        else 	throw new NoSuchElementException();

        return new Ratio(betVariation, null, ratio, type);
    }

    @Override
    public String getSelector() {
        return selector;
    }
}
