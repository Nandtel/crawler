package com.crawler.model.market;

import com.crawler.model.Ratio;
import org.jsoup.nodes.Element;

import java.util.NoSuchElementException;

public class CS implements Market {
    private final Type type;
    private final String selector;

    public CS(String selector) {
        this.selector = selector;

        this.type = Type.CS;
    }

    @Override
    public Ratio getRatio(Element element) {
        String betVariation = "CS";
        Double ratio = Double.valueOf(element.select(".eventprice").text());
        String desc = element.select(".eventselection").text();
        Double value = Double.valueOf(desc.replaceAll("-",".").replaceAll("[^0-9.,]+",""));

        if (desc.isEmpty())
            throw new NoSuchElementException();

        return new Ratio(betVariation, value, ratio, type);
    }

    @Override
    public String getSelector() {
        return selector;
    }
}
