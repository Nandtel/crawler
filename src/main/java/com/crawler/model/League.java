package com.crawler.model;

import java.util.List;

public class League {
    private final String name;
    private final List<Event> events;

    public League(String name, List<Event> events) {
        this.name = name;
        this.events = events;
    }

}
