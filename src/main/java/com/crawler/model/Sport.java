package com.crawler.model;

import java.util.List;

public class Sport {
    private final String sport;
    private final List<League> leagues;

    public Sport(String sport, List<League> leagues) {
        this.sport = sport;
        this.leagues = leagues;
    }
}
