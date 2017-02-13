package com.crawler.model.market;

import com.crawler.model.Ratio;
import org.jsoup.nodes.Element;

import java.util.NoSuchElementException;

public class DNB implements Market {
    private final Type type;
    private final String selector;
    private final String home;
    private final String away;
    private final String descStartWith;

    public DNB(String home, String away) {
        this.home = home;
        this.away = away;

        this.selector = "td";
        this.type = Type.DNB;
        this.descStartWith = "Ничья ставки нет";
    }

    @Override
    public Ratio getRatio(Element element) {
        String betVariation;
        Double ratio = Double.valueOf(element.select(".eventprice").text());
        String desc = element.select(".eventselection").text();

        if 		(home.equalsIgnoreCase(desc))	betVariation = "DNB1";
        else if (away.equalsIgnoreCase(desc))	betVariation = "DNB2";
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
