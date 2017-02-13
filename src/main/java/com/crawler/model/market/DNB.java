package com.crawler.model.market;

import com.crawler.model.Ratio;
import org.jsoup.nodes.Element;

import java.util.NoSuchElementException;

public class DNB implements Market {
    final Type type;
    final String selector;
    final String home;
    final String away;

    public DNB(String home, String away, String selector) {
        this.home = home;
        this.away = away;
        this.selector = selector;

        this.type = Type.DNB;
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
}
