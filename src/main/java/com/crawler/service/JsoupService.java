package com.crawler.service;

import com.crawler.model.Event;
import com.crawler.model.League;
import com.crawler.model.Ratio;
import com.crawler.model.Sport;
import com.crawler.model.market.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Service
public class JsoupService {

    public Document getPage(String url) throws IOException {
        return Jsoup.connect("http://sports.williamhill.com/bet/en-gb/betting/e/10582277/Burnley%20v%20Chelsea.html")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36")
                .cookie("cust_prefs", "en|DECIMAL|form|TYPE|PRICE|||0|SB|0|0||1|en|0|TIME|TYPE|0|1|A|0||0|0||TYPE||-|0")
                .get();
    }

    public Document getPage(ClassPathResource path) throws IOException {
        InputStream inputStream = new ClassPathResource("burnley-chelsea.html").getInputStream();
        return Jsoup.parse(inputStream, "UTF-8", "http://sports.williamhill.com/");
    }

    public String getName(Document page, String selector) {
        return page.select(selector).text();
    }

    public String getTeamName(Document page, String selector, Integer position) {
        return page.select(selector).text().split(" ")[position];
    }

    public String getStartedAtDate(Document page, String selector, String without) {
        return page.select(selector).text().replaceFirst(without, "").trim();
    }

    public Elements getMarketsContainer(Document page, String selector) {
        return page.select(selector);
    }

    public Map<String, List<Ratio>> getGroupedRatios(Elements marketsContainer, List<Market> markets) {
        return markets
                .parallelStream()
                .flatMap(market -> marketsContainer.parallelStream()
                        .filter(el -> el.select("thead>tr>th.leftPad>span").text().toLowerCase()
                                    .startsWith(market.getDescStartWith().toLowerCase()))
                        .map(el -> el.select(market.getSelector()))
                        .flatMap(Collection::stream)
                        .map(market::getRatio))
                .collect(groupingBy(ratio -> ratio.getType().toString()));
    }

    public String getSportJson(Document page) {
        String sportName = getName(page, "#breadcrumb li:nth-child(2) a");
        String leagueName = getName(page, "#breadcrumb li:nth-child(4) a");
        String homeTeamName = getTeamName(page, "#contentHead h2", 0);
        String awayTeamName = getTeamName(page, "#contentHead h2", 2);
        String startedAtDate = getStartedAtDate(page, "#contentHead > span#eventDetailsHeader span", "Закрытие :");
        Elements marketsContainer = getMarketsContainer(page, "div#allMarketsTab div#primaryCollectionContainer .marketHolderExpanded");

        List<Market> markets = Arrays.asList(
                new X12(homeTeamName, awayTeamName, "td"),
                new DC(homeTeamName, awayTeamName,"td"),
                new BTS("td"),
                new CS("td li"),
                new DNB(homeTeamName, awayTeamName, "td"),
                new OU("td")
        );

        Map<String, List<Ratio>> main = getGroupedRatios(marketsContainer, markets);

        Map<String, Map<String, List<Ratio>>> periods = new HashMap<>();
        periods.put("main", main);
        periods.put("time1", Collections.emptyMap());
        periods.put("time2", Collections.emptyMap());

        Event match = new Event(homeTeamName, awayTeamName, startedAtDate, periods);
        League league = new League(leagueName, Arrays.asList(match));
        Sport sport = new Sport(sportName, Arrays.asList(league));

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        return gson.toJson(sport);
    }
}
