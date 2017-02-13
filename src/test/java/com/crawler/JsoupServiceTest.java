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
import com.google.gson.stream.JsonReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

        File html = new ClassPathResource("burnley-chelsea.html").getFile();
        page = Jsoup.parse(html, "UTF-8", "http://sports.williamhill.com/");

        File json = new ClassPathResource("burnley-chelsea.json").getFile();
        JsonReader jsonReader = new JsonReader(new FileReader(json));
        Sport sport = gson.fromJson(jsonReader, Sport.class);
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
        Elements marketsContainerResult = page.select("div#allMarketsTab div#primaryCollectionContainer");

        List<Market> markets = Arrays.asList(
                new X12(homeTeamName, awayTeamName, ".marketHolderExpanded:nth-child(1) td"),
                new DC(homeTeamName, awayTeamName,".marketHolderExpanded:nth-child(25) td")
        );

        Map<String, List<Ratio>> testable = jsoupService.getGroupedRatios(marketsContainerResult, markets);
        testable.entrySet().stream().forEach(System.out::println);


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
        File file = new ClassPathResource("burnley-chelsea.html").getFile();
        String page = Jsoup.parse(file, "UTF-8", "http://sports.williamhill.com/").text();
        String testable = jsoupService.getPage(new ClassPathResource("burnley-chelsea.html")).text();

        Assert.assertEquals(page, testable);
    }

}
