package com.crawler.model.market;

import com.crawler.model.Ratio;
import org.jsoup.nodes.Element;

import java.util.NoSuchElementException;

public class DC implements Market {
    private final Type type;
    private final String selector;
    private final String home;
    private final String away;
    private final String descStartWith;

    public DC(String home, String away, String selector) {
        this.home = home;
        this.away = away;
        this.selector = selector;

        this.type = Type.DC;
        this.descStartWith = "Двойной шанс";
    }

    @Override
    public Ratio getRatio(Element element) {
        String betVariation;
        Double ratio = Double.valueOf(element.select(".eventprice").text());
        String desc = element.select(".eventselection").text();

        String first = home + " или Ничья";
        String second = away + " или Ничья";
        String both = home + " или " + away;


        if 		(first.equalsIgnoreCase(desc))	betVariation = "1X";
        else if (second.equalsIgnoreCase(desc))	betVariation = "X2";
        else if (both.equalsIgnoreCase(desc))	betVariation = "12";
        else    throw new NoSuchElementException();

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
