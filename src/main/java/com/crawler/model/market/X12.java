package com.crawler.model.market;

import com.crawler.model.Ratio;
import org.jsoup.nodes.Element;

import java.util.NoSuchElementException;

public class X12 implements Market {
    final Type type;
    final String selector;
    final String home;
    final String away;

    public X12(String home, String away, String selector) {
        this.home = home;
        this.away = away;
        this.selector = selector;

        this.type = Type.X12;
    }

    @Override
    public Ratio getRatio(Element element) {
        String betVariation;
        Double ratio = Double.valueOf(element.select(".eventprice").text());
        String desc = element.select(".eventselection").text();
        String draw = "Ничья";

        if 		(home.equalsIgnoreCase(desc))	betVariation = "1";
        else if (away.equalsIgnoreCase(desc))	betVariation = "2";
        else if (draw.equalsIgnoreCase(desc))	betVariation = "X";
        else throw new NoSuchElementException();

        return new Ratio(betVariation, null, ratio, type);
    }

    @Override
    public String getSelector() {
        return selector;
    }

}
