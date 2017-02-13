package com.crawler.model.market;

import com.crawler.model.Ratio;
import org.jsoup.nodes.Element;

public interface Market {

    Ratio getRatio(Element element);
    String getSelector();
}
