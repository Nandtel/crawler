package com.crawler.model.market;

import com.crawler.model.Ratio;
import org.jsoup.nodes.Element;

import java.util.NoSuchElementException;

public class BTS implements Market {
    private final Type type;
    private final String selector;
    private final String descStartWith;

    public BTS(String selector) {
        this.selector = selector;

        this.type = Type.BTS;
        this.descStartWith = "Обе команды забьют";
    }

    @Override
    public Ratio getRatio(Element element) {
        String betVariation;
        Double ratio = Double.valueOf(element.select(".eventprice").text());
        String desc = element.select(".eventselection").text();

        if 		("Да".equalsIgnoreCase(desc))		betVariation = "Y";
        else if ("Нет".equalsIgnoreCase(desc))	    betVariation = "N";
        else 	throw new NoSuchElementException();

        return new Ratio(betVariation, null, ratio, type);
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
