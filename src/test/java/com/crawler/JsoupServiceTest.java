package com.crawler;

import com.crawler.model.Ratio;
import com.crawler.model.Sport;
import com.crawler.model.market.DC;
import com.crawler.model.market.Market;
import com.crawler.model.market.Type;
import com.crawler.model.market.X12;
import com.crawler.service.JsoupService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
public class JsoupServiceTest {

    private static Document page;
    private static String resultJson;
    private static JsoupService jsoupService;

    @BeforeClass
    public static void setUp() throws IOException {
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

        InputStream inputStreamHtml = new ClassPathResource("burnley-chelsea.html").getInputStream();
        page = Jsoup.parse(inputStreamHtml, "UTF-8", "http://sports.williamhill.com/");

        InputStream inputStreamJson = new ClassPathResource("burnley-chelsea.json").getInputStream();
        Sport sport = gson.fromJson(new InputStreamReader(inputStreamJson, "UTF-8"), Sport.class);
        resultJson = gson.toJson(sport);

        jsoupService = new JsoupService();
    }

    @Test
    public void shouldJsonBeMatched() throws Exception {
        String testedJson = jsoupService.getSportJson(page);
        Assert.assertEquals(resultJson, testedJson);
    }

    @Test
    public void shouldNameBeMatched() {
        String sportName = jsoupService.getName(page, "#breadcrumb li:nth-child(2) a");
        Assert.assertEquals("Футбол", sportName);
    }

    @Test
    public void shouldTeamNameBeMatched() {
        String teamName = jsoupService.getTeamName(page, "#contentHead h2", 0);
        Assert.assertEquals("Бернли", teamName);
    }

    @Test
    public void shouldStartedDateBeMatched() {
        String startedAtDate = jsoupService.getStartedAtDate(page, "#contentHead > span#eventDetailsHeader span", "Закрытие :");
        Assert.assertEquals("12 Фев -13:30 UK", startedAtDate);
    }

    @Test
    public void shouldMarketsContainerBeMatched() {
        Elements marketsContainerResult = page.select("div#allMarketsTab div#primaryCollectionContainer");
        Elements marketsContainer = jsoupService.getMarketsContainer(page, "div#allMarketsTab div#primaryCollectionContainer");
        Assert.assertEquals(marketsContainerResult, marketsContainer);
    }

    @Test
    public void shouldGroupedRatiosBeMatched() {
        String homeTeamName = "Бернли";
        String awayTeamName = "Челси";
        Elements marketsContainerResult = page.select("div#allMarketsTab div#primaryCollectionContainer .marketHolderExpanded");

        List<Market> markets = Arrays.asList(
                new X12(homeTeamName, awayTeamName, "td"),
                new DC(homeTeamName, awayTeamName,"td")
        );

        Map<String, List<Ratio>> testable = jsoupService.getGroupedRatios(marketsContainerResult, markets);


        Map<String, List<Ratio>> main = new HashMap<>();
        main.put("1X2", Arrays.asList(
                new Ratio("1", null, 9.0, Type.X12),
                new Ratio("X", null, 4.5, Type.X12),
                new Ratio("2", null, 1.4, Type.X12)
        ));
        main.put("DC", Arrays.asList(
                new Ratio("1X", null, 2.62, Type.DC),
                new Ratio("X2", null, 1.05, Type.DC),
                new Ratio("12", null, 1.18, Type.DC)
        ));

        Assert.assertEquals(main, testable);
    }

    @Test
    public void shouldGetPageByPathBeMatched() throws IOException {
        InputStream inputStream = new ClassPathResource("burnley-chelsea.html").getInputStream();
        String page = Jsoup.parse(inputStream, "UTF-8", "http://sports.williamhill.com/").text();
        String testable = jsoupService.getPage(new ClassPathResource("burnley-chelsea.html")).text();

        Assert.assertEquals(page, testable);
    }

}
